package com.hls.server.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.hls.server.encoder.Encoder;

import static com.hls.server.constants.VideoConstants.*;
import static com.hls.server.constants.HeaderConstants.*;

@Service
public class VideoStreamService {
    private final Encoder encoder = new Encoder();

    /**
     * Gets HLS playlist. First call will be the master and the next will be the
     * appropriate steam playlist.
     * 
     * @param filename
     * @return
     * @throws IOException
     */
    public ResponseEntity<byte[]> readFile(String adNumber, String filename) throws IOException {
        Path file = Paths.get(OUT_FOLDER, adNumber, filename);
        var bytes = readFile(file);
        String ext = getContentTypeFromFileName(filename);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(CONTENT_TYPE, ext)
                .header(ACCEPT_RANGES, BYTES)
                .header(CONTENT_LENGTH, Integer.toString(bytes.length))
                .body(bytes);
    }

    public String encode(String adNumber) throws IOException {
        String videoFolderPath = getFilePath();
        String filePath = Paths.get(videoFolderPath, adNumber + ".MP4").normalize().toString();

        encoder.encode(Paths.get(videoFolderPath), filePath, adNumber);
        return "Success";
    }

    private byte[] readFile(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    /**
     * Get the filePath.
     *
     * @return String.
     */
    private String getFilePath() {
        URL url = this.getClass().getResource(VIDEO);
        return new File(url.getFile()).getAbsolutePath();
    }

    private String getContentTypeFromFileName(String filename) throws IllegalArgumentException {
        var ext = Optional.ofNullable(filename).map(f -> f.substring(filename.lastIndexOf(".") + 1)).get();

        switch (ext) {
            case PLAY_LIST_EXTENSION:
                return "audio/mpegurl";

            case DATA_SEGMENT_EXTENSION:
                return "application/octet-stream";

            default:
                throw new IllegalArgumentException("Unknown extension " + ext);
        }
    }
}