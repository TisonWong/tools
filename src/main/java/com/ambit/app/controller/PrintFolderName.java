package com.ambit.app.controller;

import java.io.File;

/**
 * 打印指定目录下父级文件夹名称
 */
public class PrintFolderName {

    public static void main(String[] args) {
        File folder = new File("\\\\am200\\H\\LAS");
        File folder2 = new File("\\\\am20\\J\\3DML\\3DML-ALL");
        File[] files = folder2.listFiles();
        for (File f: files){
            System.err.println(f.getName());
        }
    }
}
