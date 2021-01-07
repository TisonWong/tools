package com.ambit.app.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析照片信息
 * @author Ben
 *
 */
public class ParsePhotoTool {

	/**
	 * 递归遍历目录下所有jpg格式照片
	 * @param strPath
	 * @param photoMap
	 * @return Map<String,File> 文件名称(无后缀), 文件映射对象
	 */
	public static Map<String,File> getAllJPGFile(String strPath,Map<String,File> photoMap) {
		File dir = new File(strPath);
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) { // 判断是文件还是文件夹
					getAllJPGFile(files[i].getAbsolutePath(),photoMap); // 获取文件绝对路径
				} else if ("jpg".equals(FilenameUtils.getExtension(files[i].getName()).toLowerCase())) { // 判断文件名是否以.jpg结尾
					photoMap.put(FilenameUtils.getBaseName(files[i].getName().toLowerCase()),files[i]);
				}
			}
		}
		return photoMap;
	}

	/**
	 * 递归读取目录下所有格式为: jpg,jpeg 的图片文件
	 * @param folderPath
	 * @param photoList
	 * @return ：映射对象集合
	 */
	public List<File> recursivelyReadPhoto(File folderPath, List<File> photoList){
		if(folderPath==null){
			return photoList;
		}
		File[] files = folderPath.listFiles();
		for(File f: files){
			if(f.isFile() && f.getName().toLowerCase().endsWith("jpg")||f.getName().toLowerCase().endsWith("jpeg")){
				photoList.add(f);
			}else if(f.isDirectory()){
				photoList = recursivelyReadPhoto(f,photoList);
			}
		}
		return photoList;
	}

	public static String convertFileToHex(Path path) throws IOException {

		if (Files.notExists(path)) {
			throw new IllegalArgumentException("File not found! " + path);
		}

		StringBuilder result = new StringBuilder();
		StringBuilder hex = new StringBuilder();

		int value;
		try (InputStream inputStream = Files.newInputStream(path)) {
			while ((value = inputStream.read()) != -1) {
				hex.append(String.format("%02X ", value));
			}
			result.append(String.format("%-60s", hex));
		}
		return result.toString();
	}
}
