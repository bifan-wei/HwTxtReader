package com.bifan.txtreaderlib.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileHashUtil {
	private static byte[] createChecksum(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);
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
 
   public static String getMD5Checksum(String filePath) throws Exception {
        byte[] b = createChecksum(filePath);
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
}
