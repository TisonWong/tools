package com.ambit.app.utils;

import javafx.scene.control.Alert;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Map;

public class FileOperationUtil {

    /**
     * 获取新文件路径
     */
    private static StringBuilder nameBuilder = new StringBuilder();
    public static File updateFilePath(String exportPath, Map.Entry<String,File> entry, boolean rename){
        nameBuilder.setLength(0);
        nameBuilder.append(exportPath).append(File.separator)
                .append(entry.getValue().getParentFile().getParentFile().getName()).append(File.separator)
                .append(entry.getValue().getParentFile().getName()).append(File.separator);
        if (rename){
            nameBuilder.append(entry.getKey().substring(0,entry.getKey().lastIndexOf(".")))
                    .append(" - 😒.JPG");
        }else{
            nameBuilder.append(entry.getKey());
        }
        return new File(nameBuilder.toString());
    }

    /**
     * 执行文件复制
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileUsingFileStreams(File source, File dest)throws IOException{
        // 如果目录不存在则创建
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try(
            InputStream inputStream  = new FileInputStream(source);
            OutputStream outputStream = new FileOutputStream(dest);
         ){
            byte[] buf = new byte[2048];
            int bytesRead;
            while((bytesRead = inputStream.read(buf))>0){
                outputStream.write(buf,0,bytesRead);
            }
        }
    }

    /**
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) {
        // 如果文件不存在则创建
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try (
                FileChannel inputChannel = new FileInputStream(source).getChannel();
                FileChannel outputChannel = new FileOutputStream(dest).getChannel();
            ){
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }catch (Exception e){
            new Alert(Alert.AlertType.WARNING, source.getName() +" - 复制失败").show();
            e.printStackTrace();
        }

    }
}
