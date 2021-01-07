package com.ambit.app.controller;

import com.ambit.app.utils.FileOperationUtil;
import com.ambit.app.utils.ParsePhotoTool;
import com.ambit.app.utils.WriteDataToFile;
import org.apache.xmlbeans.impl.util.Base64;

import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

/**
 * sample1:https://blog.csdn.net/rexueqingchun/article/details/78150877
 * sample2:https://mkyong.com/java/how-to-convert-file-to-hex-in-java/
 */
public class RepairJPG {

    public static void main(String[] args) throws IOException {
        /*
        1：所有照片的十六进制数据另存到本地，每个照片单独保存一个txt，以照片名称命名
        2：读取正常照片的十六进制数据，截取0~LastIndexOf("FF D9")部分
        3：读取每个txt，从中查找并替换0~LastIndexOf("FF D9")部分
        4：将结果导出到指定文件夹中
         */
//        File goodFile = new File("D:\\@Ben\\RecoverPhoto\\JPEGS\\G0014092.JPG");
//
        String file = "D:\\@Ben\\RecoverPhoto\\JPEGS\\G0014500 1.JPG";
        String hexString = jpg2hex(file);
//
//        hexToImage("D:\\@Ben\\RecoverPhoto\\JPEGS\\output6.jpg",hexString);
//        batch1();

    }
    static void batch1() throws IOException {

        File badFolder = new File("D:\\@Ben\\RecoverPhoto\\BadJPG");
        String outputFolder = "D:\\@Ben\\RecoverPhoto\\HexFiles";
        for (File jpg: Objects.requireNonNull(badFolder.listFiles())){
            if (jpg.getName().toLowerCase().endsWith("jpg")){
                WriteDataToFile.resultWrite(jpg2hex(jpg.getPath()),outputFolder,jpg.getName().substring(0,jpg.getName().lastIndexOf(".")),false);
            }
        }
    }

    public static String jpg2hex(String sourceJPG) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream(3000000); //2444826
        System.err.println(sourceJPG);
        BufferedImage img=ImageIO.read(new File(sourceJPG));
        ImageIO.write(img, "jpg", baos);

        baos.flush();

        byte[] base64String= Base64.encode(baos.toByteArray());
        //%02X
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b:baos.toByteArray()){
            stringBuilder.append(String.format("%02X",b));
        }
//        WriteDataToFile.resultWrite(stringBuilder.toString(),"D:\\@Ben\\Test\\G0014092.txt");
        baos.close();

//        byte[] bytearray = Base64.decode(base64String);
//        BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytearray));
//        ImageIO.write(imag, "jpg", new File("D:\\@Ben\\RecoverPhoto\\JPEGS\\output6-2.jpg"));
        return stringBuilder.toString();
    }

    static void hexToImage(String filePath,String hexString) {
        byte[] bytes = stringToByte(hexString);
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(filePath));
            imageOutput.write(bytes, 0, bytes.length);
            imageOutput.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] stringToByte(String s) {
        int length = s.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((Character.digit(s.charAt(i * 2), 16) << 4) | Character.digit(s.charAt((i * 2) + 1), 16));
        }
        return bytes;
    }
}
