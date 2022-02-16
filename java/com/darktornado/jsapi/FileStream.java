package com.darktornado.jsapi;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileStream extends ScriptableObject {
    @Override
    public String getClassName() {
        return "FileStream";
    }

    @JSStaticFunction
    public static String read(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) return null;
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String str = br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                str += "\n" + line;
            }
            fis.close();
            isr.close();
            br.close();
            return str;
        } catch (Exception e) {
            //toast(e.toString());
        }
        return null;
    }

    @JSStaticFunction
    public static boolean write(String path, String value) {
        try {
            File file = new File(path);
            FileOutputStream fos = new java.io.FileOutputStream(file);
            fos.write(value.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            //toast(e.toString());
        }
        return false;
    }

    @JSStaticFunction
    public static boolean append(String path, String value) {
        String data = read(path);
        return write(path, data + value);
    }

    @JSStaticFunction
    public static boolean remove(String path) {
        File file = new File(path);
        return file.delete();
    }

}
