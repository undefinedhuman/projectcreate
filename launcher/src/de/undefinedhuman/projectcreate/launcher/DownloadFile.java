package de.undefinedhuman.projectcreate.launcher;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;

public class DownloadFile {

    public static long downloadFile(String downloadUrl, String saveAsFileName) throws IOException, URISyntaxException {
        File outputFile = new File(saveAsFileName);
        URLConnection downloadFileConnection = new URI(downloadUrl).toURL()
                .openConnection();
        return transferDataAndGetBytesDownloaded(downloadFileConnection, outputFile);
    }

    public static long downloadFileWithResume(String downloadUrl, String saveAsFileName) throws IOException, URISyntaxException {
        File outputFile = new File(saveAsFileName);

        URLConnection downloadFileConnection = addFileResumeFunctionality(downloadUrl, outputFile);
        return transferDataAndGetBytesDownloaded(downloadFileConnection, outputFile);
    }

    private static URLConnection addFileResumeFunctionality(String downloadUrl, File outputFile) throws IOException, URISyntaxException {
        long existingFileSize;
        URLConnection downloadFileConnection = new URI(downloadUrl).toURL()
                .openConnection();

        if (outputFile.exists() && downloadFileConnection instanceof HttpURLConnection) {
            HttpURLConnection httpFileConnection = (HttpURLConnection) downloadFileConnection;

            HttpURLConnection tmpFileConn = (HttpURLConnection) new URI(downloadUrl).toURL()
                    .openConnection();
            tmpFileConn.setRequestMethod("HEAD");
            long fileLength = tmpFileConn.getContentLengthLong();
            existingFileSize = outputFile.length();

            if (existingFileSize < fileLength) {
                httpFileConnection.setRequestProperty("Range", "bytes=" + existingFileSize + "-" + fileLength);
            } else {
                throw new IOException("File Download already completed.");
            }
        }
        return downloadFileConnection;
    }

    private static long transferDataAndGetBytesDownloaded(URLConnection downloadFileConnection, File outputFile) throws IOException {
        long bytesDownloaded = 0;
        try (InputStream is = downloadFileConnection.getInputStream(); OutputStream os = new FileOutputStream(outputFile, true)) {

            byte[] buffer = new byte[1024];

            int bytesCount;
            while ((bytesCount = is.read(buffer)) > 0) {
                os.write(buffer, 0, bytesCount);
                bytesDownloaded += bytesCount;
            }
        }
        return bytesDownloaded;
    }

}
