package com.ambit.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件分块工具
 */
public class FileBlockTool {


    public static void main(String[] args) {
        FileBlockTool fileBlockTool = new FileBlockTool();
        // 执行文件分块
        File sourceFile = new File("\\\\am20\\CC_PROJECTS(D)\\CC_Project\\20200508_HK_MPT_DTM\\20200520_LansD_MPT\\20200521_LansD_MPT\\20200521_LansD_MPT\\HK_MPT_0521\\0521_lansd_mpt.mpt");
        File outputFolder = new File("\\\\am20\\CC_PROJECTS(D)\\CC_Project\\20200508_HK_MPT_DTM\\20200520_LansD_MPT\\20200521_LansD_MPT\\20200521_LansD_MPT\\HK_MPT_0521\\output");
        try{
            fileBlockTool.chunkFile(sourceFile,outputFolder,20480L);
        }catch (Exception e){e.printStackTrace();}

    }

    /**
     * 文件分块
     * @param sourceFile : 源文件路径以及名称
     * @param outputFolder : 分块文件输出目录
     * @param chunkFileSize : 定义每块文件的大小,单位: MB
     * @throws IOException
     */
    public void chunkFile(File sourceFile, File outputFolder, Long chunkFileSize)throws IOException{

        // 兆字节转比特  (比特/1024)= 千字节/1024 = 兆字节
        long chunkFileSizeToByte =  ((chunkFileSize*1024)*1024);
        // 定义块文件大小
        long chunkFileNum = (long)Math.ceil(sourceFile.length() * 1.0 / chunkFileSizeToByte);// 四舍五入long chunkFileNum = 4;
        // 读取文件对象
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");

        byte[] b = new byte[1024];

        // 块文件对象
        File chunkFile = null;
        int len = -1;
        // 写入文件对象
        RandomAccessFile raf_write = null;

        for(int i =0; i< chunkFileNum; i++){
            System.out.println("正在分块: "+(i+1)+"/"+chunkFileNum);
            chunkFile = new File(outputFolder.getPath()+File.separator+i);
            System.out.println("文件大小:"+((sourceFile.length()/1024)/1024)+"兆,共分成:"+chunkFileNum+"块");

//            raf_write = new RandomAccessFile(chunkFile,"rw");
//            while ((len = raf_read.read(b))!=-1){
//                raf_write.write(b,0,len);
//                if(chunkFile.length() >= chunkFileSizeToByte){
//                    break;
//                }
//            }
//            raf_write.close();
        }
        if (1==1){return;}

        raf_read.close();
        System.out.println("SUCCESS");
    }
}
