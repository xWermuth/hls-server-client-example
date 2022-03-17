package com.hls.server.encoder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;

import static com.hls.server.constants.VideoConstants.*;

public class Encoder {
    private final String PRESET = "slow";
    private final Fs fs = new Fs();

    public FFmpegResult encode(Path ffmpegPath, String filePath, String adNumber) throws IOException {

        fs.createDirInWorkingDirectory(OUT_FOLDER);
        fs.createDir(Paths.get(OUT_FOLDER, adNumber).toString());

        String outputStreamSegments = Paths.get(OUT_FOLDER, adNumber, DATA_SEGMENT_NAME).toString();
        String streamOuput = Paths.get(OUT_FOLDER, adNumber, STREAM_NAME).toString();

        return FFmpeg.atPath(ffmpegPath).addArguments("-i",
                filePath)
                .addArguments("-filter_complex",
                        "[0:v]split=3[v1][v2][v3]; [v1]copy[v1out]; [v2]scale=w=iw/2:h=ih/2[v2out]; [v3]scale=w=iw/4:h=ih/4[v3out]")
                // Map first
                .addArguments("-map", "[v1out]")
                .addArguments("-c:v:0", "libx264")
                .addArguments("-x264-params", "nal-hrd=cbr:force-cfr=1")
                .addArguments("-b:v:0", "5M")
                .addArguments("-maxrate:v:0", "5M")
                .addArguments("-minrate:v:0", "5M")
                .addArguments("-bufsize:v:0", "10M")
                .addArguments("-preset", PRESET)
                .addArguments("-g", "48")
                .addArguments("-sc_threshold", "0")
                .addArguments("-keyint_min", "48")
                // Map next
                .addArguments("-map", "[v2out]")
                .addArguments("-c:v:1", "libx264")
                .addArguments("-x264-params", "nal-hrd=cbr:force-cfr=1")
                .addArguments("-b:v:1", "3M")
                .addArguments("-maxrate:v:1", "3M")
                .addArguments("-minrate:v:1", "3M")
                .addArguments("-bufsize:v:1", "3M")
                .addArguments("-preset", PRESET)
                .addArguments("-g", "48")
                .addArguments("-sc_threshold", "0")
                .addArguments("-keyint_min", "48")
                // Map next
                .addArguments("-map", "[v3out]")
                .addArguments("-c:v:1", "libx264")
                .addArguments("-x264-params", "nal-hrd=cbr:force-cfr=1")
                .addArguments("-b:v:1", "1M")
                .addArguments("-maxrate:v:2", "1M")
                .addArguments("-minrate:v:2", "1M")
                .addArguments("-bufsize:v:2", "1M")
                .addArguments("-preset", PRESET)
                .addArguments("-g", "48")
                .addArguments("-sc_threshold", "0")
                .addArguments("-keyint_min", "48")
                // Do something
                .addArguments("-map", "a:0")
                .addArguments("-c:a:0", "aac")
                .addArguments("-b:a:0", "96k")
                .addArguments("-ac", "2")
                // 2
                .addArguments("-map", "a:0")
                .addArguments("-c:a:1", "aac")
                .addArguments("-b:a:1", "96k")
                .addArguments("-ac", "2")
                // 3
                .addArguments("-map", "a:0")
                .addArguments("-c:a:2", "aac")
                .addArguments("-b:a:2", "48k")
                .addArguments("-ac", "2")
                // Configure hls
                .addArguments("-f", "hls")
                .addArguments("-hls_time", "2") // TODO change to 10 seconds
                .addArguments("-hls_playlist_type", "vod")
                .addArguments("-hls_flags", "independent_segments")
                .addArguments("-hls_segment_type", STREAM_SEGMENT_TYPE)
                .addArguments("-master_pl_name", MASTER_PLAYLIST)
                .addArguments("-hls_segment_filename", outputStreamSegments)
                // .addArguments("-hls_base_url", "resources/")
                // .addArguments("-strftime_mkdir", "1")
                .addArguments("-var_stream_map", "v:0,a:0 v:1,a:1 v:2,a:2")
                .addArgument(streamOuput)
                .execute();
    }
}
