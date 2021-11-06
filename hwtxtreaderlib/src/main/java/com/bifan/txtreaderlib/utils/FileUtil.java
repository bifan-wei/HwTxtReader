package com.bifan.txtreaderlib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;


public class FileUtil {
    public static String getMD5Checksum(String filePath) throws Exception {
        byte[] b = createChecksum(filePath);
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }


    public static Boolean FileExist(String path) {
        if (path != null && path.length() > 0) {
            File file = new File(path);
            return file.exists();
        }
        return false;
    }

    public static String getDefaultNameFromFilePath(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return "";
        }
        File file = new File(filePath);
        if (file.exists()) {
            return file.getName();
        }
        return filePath;
    }

    private static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }
}
