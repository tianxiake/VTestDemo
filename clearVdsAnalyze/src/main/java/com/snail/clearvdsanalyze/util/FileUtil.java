package com.snail.clearvdsanalyze.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yongjie created on 2019-08-04.
 */
public class FileUtil {

    public static boolean writeToFile(File file, String content, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, append);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            return true;
        } catch (Exception e) {
            Log.toAppendFile("写文件异常", e);
            return false;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {

            }
        }
    }
}
