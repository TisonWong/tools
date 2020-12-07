package com.ambit.app.controller;

import com.ambit.app.utils.ParseDocumentTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * 筛选GPS时间记录
 */
public class GPSDataController {
    public static void main(String[] args) {

        try{

            String file = "C:\\Users\\Ben\\Downloads\\00000364(00441_20191205062141).19o";

            File outputFile = new File("C:\\Users\\Ben\\Downloads\\output.txt");
            if(!outputFile.exists()){outputFile.createNewFile();}

            System.err.println(outputFile.getPath());

            FileWriter fileWriter = new FileWriter(outputFile,false);
            PrintWriter printWriter = new PrintWriter(fileWriter);


            ArrayList<String> dataList = ParseDocumentTool.readFullDocument(file);


            for (String str: dataList){
                if(str.startsWith(">") && str.trim().endsWith("5  1")){
                    printWriter.println(str);
                }
            }

            printWriter.flush();
            fileWriter.flush();

            fileWriter.close();
            printWriter.close();

        }catch (IOException e){e.printStackTrace();}

    }
}
