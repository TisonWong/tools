package com.ambit.app.controller;

import com.ambit.app.utils.ParseDocumentTool;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * 移动OSGB文件夹
 */
public class MoveOSGBFolder {

    public static void main(String[] args) {
        MoveOSGBFolder moveOSGBFolder = new MoveOSGBFolder();
        String sourceFolder = "\\\\am14\\E\\PlanD_OpenData_Download\\OSGB\\2st";
        String output = "\\\\am14\\E\\PlanD_OpenData_Download\\OSGB\\2st_output";

        String testSourceFolder = "D:\\TestData\\Test@OsgbFilesMove_2020-05-29\\TestSource";
        String testOutput = "D:\\TestData\\Test@OsgbFilesMove_2020-05-29\\TestOutput";

        moveOSGBFolder.startMove(sourceFolder, output);
    }


    public void startMove(String folderPath, String outputFolderPath) {
        File folder = new File(folderPath);
        System.out.println("读取目标目录...\n");
        if (!folder.exists()) {
            System.out.println("源文件夹不存在 ！！！\n");
        } else {
            // 根据路径读取目录下所有文件的映射对象数组
            List<File> allFileList = new ArrayList<>();
            Set<String> extensionSet = new HashSet<>();
            ParseDocumentTool.findAllFileByFolderPath(new File(folderPath), allFileList, extensionSet);

            System.out.println("读取到的文件类型包含以下");
            System.err.println(Arrays.toString(extensionSet.toArray()));
            System.out.println("输入需要排除的文件格式, 多种格式时以','号隔开, 无需排除直接按回车");
            Scanner scanner = new Scanner(System.in);
            String excludeExtension = scanner.next();
            System.err.println(excludeExtension);

            //  需要排除的文件格式
            List<String> excludeExtensionList = new ArrayList<>();
            if (null != excludeExtension){
                String[] exs = excludeExtension.split(",");
                excludeExtensionList = Arrays.asList(exs);
            }

            if (allFileList.size() < 1) {
                System.out.println("输入的文件路径下为空！！！\n");
            } else {
                System.out.println("目标目录读取完毕√\n");
                System.out.println("执行文件移动...\n");
                int excludeCount = excludeExtensionList.size();
                File newFile;
                for (File f: allFileList){
                    if (excludeCount > 1){
                        if (excludeExtensionList.contains(FilenameUtils.getExtension(f.getName()))){
                            System.err.println("跳过: "+f.getName());
                            continue;
                        }
                    }
                    // 导出路径 + 原父级目录名称 + / + 原文件名称
                    newFile = new File(outputFolderPath,f.getParentFile().getName() +File.separator+ f.getName());
                    if (!newFile.getParentFile().exists()){
                        if (!newFile.getParentFile().mkdirs()){
                            System.err.println("创建失败 :"+newFile.getParent());
                            continue;
                        }
                    }
                   f.renameTo(newFile);

                }
                System.out.println("所有文件移动完毕 √\n");
            }
        }
    }

    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        String[] filePath = file.list();
        File[] fileArray = file.listFiles();
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }
        for(int i = 0; i < filePath.length; ++i) {
            if ((new File(oldPath + File.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

            if ((new File(oldPath + File.separator + filePath[i])).isFile()) {
                moveFile(oldPath + File.separator + filePath[i], newPath + File.separator + fileArray[i].getParentFile().getName() + File.separator + filePath[i]);
            }
        }
    }

    public static void moveFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        File parentPath = new File(newFile.getParent());
        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }
        oldFile.renameTo(newFile);
    }



}
