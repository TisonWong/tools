package com.ambit.app.controller;

import com.ambit.app.utils.ParseDocumentTool;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改照片的名称
 */
public class RenameImage {

    public static void main(String[] args) {
        List<File> fileList = new ArrayList<>();
        ParseDocumentTool.findAllFileByFolderPath(new File("D:\\Tasks\\2020-08-10(TE4W街景照片准备)\\imgPath"),fileList);
        for (File f: fileList){
            System.out.println(f.renameTo(new File(f.getParentFile(),"A.JPG")));
//            fileEntry.getValue().renameTo(new File(fileEntry.getValue().getParentFile(),"A.JPG"));
        }
    }

}
