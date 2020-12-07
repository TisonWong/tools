package com.ambit.app.controller;



import com.ambit.app.utils.ParseDocumentTool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CheckFile {

    public static void main(String[] args) {
        f3();
    }

    /**
     * 将文件夹下所有文件列出
     */
    public static void f3(){
        File folder = new File("D:\\Jar程序源文件\\tools(常用工具集)\\tools\\lib");
        for (File f: folder.listFiles()){
            System.err.println(f.toPath());
        }
    }

    /**
     * 比对恢复的文件是否有缺失
     */
    public static void f2(){
        File targetFolder = new File("\\\\am2\\J\\LAS");
        File sourceFolder = new File("F:\\CPT");
        // 读取源数据盘的文件结构
        File[] targetFiles = targetFolder.listFiles();

        Map<String,File> targetFileMap = new HashMap<>();
        for(File f:targetFiles){
            targetFileMap.put(f.getName(),f);
        }

        File[] sourceFiles = sourceFolder.listFiles();
        for (File f: sourceFiles){
            if (null == targetFileMap.get(f.getName())){
                System.err.println(f.getName());
            }
        }

    }

    /**
     * 根据txt记录的文件名称查找目录下是否存在相应文件
     * 并把有记录却无文件的信息打印
     */
    public void f1(){
        // 文件所在目录: \\AM20\aserver_1\AServer_Projects\20180823_LandsD_Aerial_Survey Project_GCPs(AMBIT)\GCP_Summary\Sub_controlpoint\GCP总_Record
        String path = "\\\\AM20\\aserver_1\\AServer_Projects\\20180823_LandsD_Aerial_Survey Project_GCPs(AMBIT)\\GCP_Summary\\Sub_controlpoint\\GCP总_Record";
        // 1:解析txt文档记录的数据
        ArrayList<String> arrayList = ParseDocumentTool.readFullDocument("C:\\Users\\Ben\\Downloads\\新建文本文档.txt");

        // 2:读取目录下所有文件的信息
        File file = new File(path);
        File[] files = file.listFiles();

        Map<String,File> fileMap = new HashMap<>();

        // 3:数据比对
        for(File f: files){
            fileMap.put(f.getName().substring(0,f.getName().lastIndexOf(".")),f);
        }

        int n=0;
        String tempStr = null;
        for(String str: arrayList){
            tempStr = str.substring(str.lastIndexOf("\\")+1,str.length());

            if(fileMap.get(tempStr)==null){
                n++;
                System.err.println(n+":"+tempStr);
            }
        }

    }
}
