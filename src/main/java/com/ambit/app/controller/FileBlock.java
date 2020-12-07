package com.ambit.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;



/**
 * File Merge
 * 文件合并
 */
public class FileBlock {

    public static void main(String[] args) {
        try{
            FileBlock fileBlockController = new FileBlock();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please entry source folder path");
            String sourcePath = scanner.nextLine();
            System.out.println("Please entry output file path and file name");
            String outputPath = scanner.nextLine();
            fileBlockController.mergeFile(new File(sourcePath),new File(outputPath));
        }catch (IOException e){e.printStackTrace();}
    }

    private void mergeFile(File sourceFolder, File outputFile)throws IOException {
        File[] files = sourceFolder.listFiles();

        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File o1, File o2) {
                if((Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName()))){
                    return 1;
                }
                return -1;
            }
        });

        boolean newFile = outputFile.createNewFile();

        RandomAccessFile raf_write = new RandomAccessFile(outputFile,"rw");

        RandomAccessFile raf_read = null;
        int n=0;
        int len = -1;
        byte[] b = new byte[1024];
        for(File chunkFile : fileList){
            n++;
            System.out.println("File Merge : "+n+"/"+(fileList.size()+1));
            raf_read = new RandomAccessFile(chunkFile,"r");
            while ((len = raf_read.read(b))!=-1){
                raf_write.write(b,0,len);
            }
            raf_read.close();
        }
        raf_write.close();
    }

}
