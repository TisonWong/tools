package com.ambit.app.controller;

import com.ambit.app.utils.ParseDocumentTool;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * 根据第一个文件的名称修改父级文件夹名称
 * 2-SW-22D-2
 *  Tile_+116_+243.osgb -- 取这个文件的名称作为父级文件夹的名称
 *  Tile_+116_+243_L1_0.osgb
 *  ...
 */
public class ByFileNameChangeFolderName {

    private static File sourceFolder = new File("\\\\Am200\\l\\TuenMun\\osgb");
    private static File subFolder = new File("D:\\TestData\\Test@ByFileNameChangeFolderName");
    private static File subFolder2 = new File("\\\\am200\\l\\TuenMun\\Final Submission\\osgb\\2-SW-22D\\data");


    public static void main(String[] args) {
        // 2-SW-21B\data\2-SW-21B-1\Tile_+105_+247.osgb
        // 1 读取目录下所有osgb文件,保存为Set
        List<File> allFiles = new ArrayList<>();
        ParseDocumentTool.findAllFileByFolderPath(sourceFolder, allFiles);

        allFiles.sort((o1, o2) -> {
            // 按照名称长度升序
            return o1.getName().length() < o2.getName().length() ? -1:0;
        });
        // 2 筛选文件,每个文件夹只保留一个
        // key: 文件夹名称, value: 文件映射对象
        Map<String,File> eqFileMap = new HashMap<>();
        for (File file : allFiles) {
            if (file.getName().endsWith(".osgb") && !eqFileMap.containsKey(file.getParentFile().getName())){
                eqFileMap.put(file.getParentFile().getName(),file);
            }
        }
        String fileName;
        for (Map.Entry<String,File> fileEntry:eqFileMap.entrySet()){
            fileName = FilenameUtils.getBaseName(fileEntry.getValue().getName());
            System.err.println(fileEntry.getValue().getParentFile().getName()+"\t\t"+fileName);
            // 执行名称修改
            fileEntry.getValue().getParentFile().renameTo(new File(fileEntry.getValue().getParentFile().getParentFile(), fileName));
        }
    }

//    public static void testSort(){
//        List<File> allFiles = new ArrayList<>();
//        ParseDocumentTool.findAllFileByFolderPath(subFolder, allFiles);
//        allFiles.sort(new Comparator<File>() {
//            @Override
//            public int compare(File o1, File o2) {
//
//                return o1.getName().length() < o2.getName().length() ? -1:0;
//            }
//        });
//        for (File allFile : allFiles) {
//            System.out.println(allFile.getName());
//        }
//    }

}
