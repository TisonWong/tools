package com.ambit.app.controller;

import com.ambit.app.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 释放指定类型文件夹
 */
public class MoveFilesByFolderName {

    /**
     * 源文件所在目录
     */
    private static File sourceFolder = new File("\\\\am200\\G\\CPT");

    public static void main(String[] args){
        // 1, 读取目录下所有文件
        List<File> fileList= ParseDocumentTool.findAllFileByFolderPath(sourceFolder,new ArrayList<>());
//        System.err.println("该目录下共有:["+fileList.size()+"]个文件"); //递归结果正常
        Map<File,File> fileMap = new HashMap<>();
        for (File f: fileList){
            if(f.getParent().endsWith("PointCloud(Geo)(Las)")){
                fileMap.put(f,new File(f.getParentFile().getPath()+File.separator+f.getName()));
                System.err.println(f.getPath()+"|||"+f.getParentFile().getParentFile()+File.separator+f.getName());
                if(f.getParentFile().getParent().equals("Point Cloud")){
                    fileMap.put(f,new File(f.getParentFile().getParent()+File.separator+f.getName()));
                    System.err.println(f.getPath()+"|||"+f.getParentFile().getParentFile().getParentFile()+File.separator+f.getName());
                }
            }
        }
    }

    /**
     * 执行文件移动
     */
    public void moveFile(Map<File,File> fileMap){
        for(Map.Entry<File,File> entryFile: fileMap.entrySet()){
            entryFile.getKey().renameTo(entryFile.getValue());
        }
    }
}
