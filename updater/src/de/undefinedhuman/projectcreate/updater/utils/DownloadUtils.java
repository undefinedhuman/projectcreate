package de.undefinedhuman.projectcreate.updater.utils;

import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class DownloadUtils {

    public static String SERVER_URL = "http://playprojectcreate.com/";
    public static String DOWNLOAD_FILE_EXTENSION = ".jar";

    public static long downloadFile(String downloadUrl, FsFile destination) throws IOException, URISyntaxException {
        URLConnection downloadFileConnection = createURLConnection(downloadUrl, destination);
        if(downloadFileConnection == null)
            return destination.length();
        return transferDataAndGetBytesDownloaded(downloadFileConnection, destination);
    }

    public static long fetchFileSize(String downloadUrl) {
        HttpURLConnection httpConnection;
        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("HEAD");
        } catch (IOException e) {
            Log.error("Error while fetching the file size of: " + downloadUrl);
            return 0;
        }
        return httpConnection.getContentLengthLong();
    }

    private static URLConnection createURLConnection(String downloadUrl, FsFile destination) throws IOException, URISyntaxException {
        URLConnection downloadFileConnection = new URI(downloadUrl).toURL().openConnection();

        if (destination.exists() && downloadFileConnection instanceof HttpURLConnection) {
            HttpURLConnection httpFileConnection = (HttpURLConnection) downloadFileConnection;

            long sourceFileSize = fetchFileSize(downloadUrl);
            long destinationFileSize = destination.length();

            if(destinationFileSize > sourceFileSize)
                destination.delete();
            else if(destinationFileSize < sourceFileSize)
                httpFileConnection.setRequestProperty("Range", "bytes=" + destinationFileSize + "-" + sourceFileSize);
            else
                return null;
        }
        return downloadFileConnection;
    }

    private static long transferDataAndGetBytesDownloaded(URLConnection downloadFileConnection, FsFile destination) throws IOException {
        long bytesDownloaded = 0;
        try (InputStream is = downloadFileConnection.getInputStream(); OutputStream os = destination.write(true)) {
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
