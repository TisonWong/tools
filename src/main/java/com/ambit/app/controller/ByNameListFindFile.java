package com.ambit.app.controller;

import com.ambit.app.utils.FileOperationUtil;
import com.ambit.app.utils.ParseDocumentTool;
import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.commons.io.FilenameUtils;

import javax.tools.DocumentationTool;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据文件名称查找文件并复制到指定文件夹
 */
public class ByNameListFindFile {


    public static void main(String[] args) throws IOException {
        String fileName = "D:\\TestData\\Test@ByNameListFindFile\\name.txt";
        ArrayList<String> arrayList = ParseDocumentTool.readFullDocument(fileName);

        List<File> fileList = new ArrayList<>();
        File folder = new File("\\\\am12\\f\\3DML\\3DML-ALL");
        ParseDocumentTool.findAllFileByFolderPath(folder,fileList);

        File outFolder = new File("F:\\香港岛");
        String folderName = null;
        for (File file : fileList) {
            if (!FilenameUtils.getExtension(file.getName()).equals("3dml")){
                continue;
            }
            folderName = file.getParentFile().getParentFile().getParentFile().getName();
//            System.err.println(folderName);
            for (String name : arrayList) {
                if (folderName.equals(name.replace("_","-"))){
                    System.err.println(file.getName()+"\tCopy To\t"+outFolder);
                    FileOperationUtil.copyFileUsingFileStreams(file,new File(outFolder,file.getName()));
                }
            }
        }
    }


}
