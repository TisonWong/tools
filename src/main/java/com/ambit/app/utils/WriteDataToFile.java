package com.ambit.app.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteDataToFile {

    /**
     * 打印内容到文件
     * @param outputData：内容
     * @param outputFilePath：输出文件路径
     * @param fileName：文件名称
     * @param append: 是否追加写入
     * @return
     */
    public static boolean resultWrite(String outputData,String outputFilePath,String fileName,Boolean append) {
                FileWriter fw = null;

        try {

            File outputFile = new File(outputFilePath,fileName); //文件名称以及路径
            if (!outputFile.exists()){
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }
            fw = new FileWriter(outputFile, append);
            PrintWriter pw = new PrintWriter(fw);


            pw.println(outputData); //打印的内容

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
