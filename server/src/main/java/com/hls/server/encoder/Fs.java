package com.hls.server.encoder;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

public class Fs {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Creates directory in working directory if it does not exists
     * 
     * @param dirName
     * @throws IOException
     */
    public void createDirInWorkingDirectory(String dirName) throws IOException {
        File directory = new File(dirName);

        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * Creates directory. If directory exists it deletes the existing dir beforehand
     * 
     * @param dirName
     * @throws IOException
     */
    public void createDir(String dirName) throws IOException {
        File directory = new File(dirName);

        if (directory.exists()) {
            deleteDir(directory);
        }

        directory.mkdir();
    }

    public void deleteDir(File dir) throws IOException {
        logger.info("Deleting directory: " + dir.getName());
        FileSystemUtils.deleteRecursively(dir);
    }
}
