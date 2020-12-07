package com.ambit.app.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteDataToFile {

    public static boolean resultWrite() {
                FileWriter fw = null;

        try {

            File outputFile = new File("输出文件路径及名称");
            fw = new FileWriter(outputFile, true);
            PrintWriter pw = new PrintWriter(fw);


            pw.println("输入信息");

            pw.flush();
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
