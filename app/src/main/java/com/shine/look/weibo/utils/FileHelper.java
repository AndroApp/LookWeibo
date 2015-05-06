package com.shine.look.weibo.utils;

import android.content.Context;

import com.shine.look.weibo.WeiboApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * User:Shine
 * Date:2015-05-05
 * Description:
 */
public class FileHelper {

    public static void saveFile(String fileName, String text) {
        Context context = WeiboApplication.getContext();
        FileOutputStream out;
        BufferedWriter writer = null;
        try {
            fileName = getFileNameByUrl(fileName);
            out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String loadFile(String fileName) {
        Context context = WeiboApplication.getContext();
        FileInputStream in;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            fileName = getFileNameByUrl(fileName);
            in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    private final static String getFileNameByUrl(String url) {
        return Utils.getMD5(url.substring(0, url.indexOf('?')));
    }

}
