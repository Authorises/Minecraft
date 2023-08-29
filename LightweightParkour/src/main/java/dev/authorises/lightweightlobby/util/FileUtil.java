package dev.authorises.lightweightlobby.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
    public static void addDirectoryToTar(TarArchiveOutputStream out, File dir, String basePath) throws IOException {
        // Iterate over all files and directories in the directory
        for (File file : dir.listFiles()) {
            String entryName = basePath + file.getName();
            if (file.isDirectory()) {
                // If the file is a directory, add it to the archive and call this method recursively
                TarArchiveEntry entry = new TarArchiveEntry(file, entryName + "/");
                out.putArchiveEntry(entry);
                out.closeArchiveEntry();
                addDirectoryToTar(out, file, entryName + "/");
            } else {
                // If the file is a regular file, add it to the archive
                TarArchiveEntry entry = new TarArchiveEntry(file, entryName);
                out.putArchiveEntry(entry);
                IOUtils.copy(new FileInputStream(file), out);
                out.closeArchiveEntry();
            }
        }
    }


}
