package com.ambit.app.utils;

import com.ambit.app.entity.GpsInfoEntity;
import com.ambit.app.process.ExecutorProcessPool;
import com.ambit.app.process.ExecutorServiceFactory;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ParseDocumentTool {

	/**
	 * 递归获取文件夹下的所有文件
	 * @param folderFile
	 * @param fileMap<"文件名（無後綴）","文件映射對象">
	 * @param type : 文件类型匹配 （*：不限制， txt: 匹配txt, las: 匹配las .....）
	 * @return
	 */
	public static Map<String,File> getAllFiles(File folderFile, Map<String,File> fileMap, final String type){
		File[] files = folderFile.listFiles();
		String extension;

		if (null == files){
			return fileMap;
		}

		for (File f1: files){
			if(f1.isFile()){
				if ("*".equals(type)){
					fileMap.put(FilenameUtils.getBaseName(f1.getName()),f1);
					continue;
				}
				extension = FilenameUtils.getExtension(f1.getName());
				if(extension.toLowerCase().equals(type.toLowerCase())){
					fileMap.put(FilenameUtils.getBaseName(f1.getName()),f1);
				}
			}else if(f1.isDirectory()){
				fileMap = getAllFiles(f1, fileMap, type);
			}
		}
		return fileMap;
	}
	/**
	 * 递归获取文件夹下的所有文件
	 * @param folderFile
	 * @param fileList<"文件映射對象">
	 * @param type : 文件类型匹配 （*：不限制， txt: 匹配txt, las: 匹配las .....）
	 * @return
	 */
	public static List<File> getAllFiles(File folderFile, List<File> fileList, final String type){
		File[] files = folderFile.listFiles();
		String extension;

		if (null == files){
			return fileList;
		}

		for (File f1: files){
			if(f1.isFile()){
				if ("*".equals(type)){
					fileList.add(f1);
					continue;
				}
				extension = FilenameUtils.getExtension(f1.getName());
				if(extension.toLowerCase().equals(type.toLowerCase())){
					fileList.add(f1);
				}
			}else if(f1.isDirectory()){
				fileList = getAllFiles(f1, fileList, type);
			}
		}
		return fileList;
	}

	/**
	 * 递归解析文件夹下的所有txt文档及其内容
	 * @param txtFolderFile
	 * @param txtMap
	 * @return
	 */
	public static Map<String, ArrayList<String>> readFullTXTDocument(File txtFolderFile,Map<String, ArrayList<String>> txtMap){
		File[] files = txtFolderFile.listFiles();
		if (null == files){return txtMap;}
		for(File f1: files){
			if(f1.isFile() && f1.getName().endsWith("txt")){
				ArrayList<String> list= readFullDocument(f1.getPath());
				if(list!=null){
					txtMap.put(f1.getName().substring(0,f1.getName().lastIndexOf(".")),list);
				}else{
					System.err.println(f1.getName()+" 文档中无数据 ");
				}
			}else if(f1.isDirectory()){
				// 递归调用
				txtMap = readFullTXTDocument(f1,txtMap);
			}
		}
		return txtMap;
	}

	/**
	 * 遍历读取指定文件夹下的所有文件(非递归)
	 * @param folderFile
	 * @param fileList
	 * @param type：指定文件后缀（如"jpg"/"cr2")不区分大小写，不需要加“.”
	 */
	public static void getAllFilesByFormat(File folderFile, List<File> fileList, final String type){
		File[] files = folderFile.listFiles();
		String extension;
		for (File f1: files){
			if(f1.isFile()){
				if ("*".equals(type)){
					fileList.add(f1);
					continue;
				}
				extension = FilenameUtils.getExtension(f1.getName());
				if(extension.toLowerCase().equals(type.toLowerCase())){
					fileList.add(f1);
				}
			}
		}
	}

	/**
	 *  获取文件夹名称列表
	 *  根据给定的路径读取文件
	 * @return : 该文件的内容（每一行为一个对象）
	 */
	public static ArrayList<String> readFullDocument(String path){
		//定义文件路径
		ArrayList<String> filenames = new ArrayList<>();
//		try{
////			FileReader fr = new FileReader(path);
//			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path),"UTF-8");
//			BufferedReader br = new BufferedReader(inputStreamReader);
//			String filenameString="";
//			while((filenameString = br.readLine())!=null){
//				filenames.add(filenameString);
//			}
//			inputStreamReader.close();
//			br.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		try(
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(inputStreamReader);
		){
			String filenameString="";
			while((filenameString = br.readLine())!=null){
				filenames.add(filenameString);
			}
		}catch (Exception e){e.printStackTrace();}

		return filenames;
	}


	/**
	 * 根据目录路径, 递归该目录下所有的文件
	 * @param folder： 需要遍历的目录路径映射对象
	 * @param fileList: 传入一个初始化的ArrayList
	 * @return: 所有文件的映射对象
	 */
	public static List<File> findAllFileByFolderPath(File folder, List<File> fileList){
		File[] files = folder.listFiles();
		if (null == files){
			return fileList;
		}
		for (File f: files){
			if (f.isFile()){
				fileList.add(f);
			}else if (f.isDirectory()){ // 如果是目录
				fileList = findAllFileByFolderPath(f,fileList);
			}
		}
		return fileList;
	}

	/**
	 *根据目录路径, 递归该目录下所有的文件
	 * @param folder
	 * @param fileList
	 * @param extensionSet : 保存读取到文件的类型(尾缀)
	 * @return
	 */
	public static List<File> findAllFileByFolderPath(File folder, List<File> fileList, Set<String> extensionSet){
		File[] files = folder.listFiles();
		if (null == files){
			return fileList;
		}
		for (File f: files){
			if (f.isFile()){
				extensionSet.add(FilenameUtils.getExtension(f.getName()));
				fileList.add(f);
			}else if (f.isDirectory()){ // 如果是目录
				fileList = findAllFileByFolderPath(f, fileList, extensionSet);
			}
		}
		return fileList;
	}


	/**
	 * 解析指定格式的XML
	 * @param atXMLFile
	 * @return Map<"照片名称.jpg",GpsInfoEntity>
	 * @throws Exception
	 */
	public static Map<String,GpsInfoEntity> parseAtXML(File atXMLFile) throws Exception{
		Map<String, GpsInfoEntity> atXmlMap = new HashMap<>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();

		// 将给定的URL的内容解析为一个XML文档，并返回Document对象
		Document document = db.parse(atXMLFile);

		// 按文档顺序返回包含在文档中且具有给定标记名称的所有Element 的NodeList
		NodeList atList = document.getElementsByTagName("Photogroups");

		StringBuilder stringBuilder = new StringBuilder();
		GpsInfoEntity gpsInfoEntity = null;

		Node photoGroups = null;
		NodeList photoGroupsList = null;
		Node photoGroup = null;
		NodeList photoGroupList = null;

		NodeList photoList = null;
		NodeList poseList = null;

		NodeList metadataList = null;
		NodeList centerList = null;
		for(int i=0; i<atList.getLength();i++){
			photoGroups = atList.item(i); // 表示每一个Photogroups
			photoGroupsList = photoGroups.getChildNodes(); //获取Photogroups下每一个<Photogroup>节点
			for(int n=0;n<photoGroupsList.getLength();n++){
				photoGroup = photoGroupsList.item(n); // 获取所有<Photogroup>节点
//                System.out.println(photoGroup.getNodeName());   //Photogroup
				photoGroupList = photoGroup.getChildNodes();
				for(int photoItem=0;photoItem<photoGroupList.getLength();photoItem++){
					if(photoGroupList.item(photoItem).getNodeName().equals("Photo")){
						photoList = photoGroupList.item(photoItem).getChildNodes(); // 获取<Photo> 下的所有子节点
						for (int l=0;l<photoList.getLength();l++){
							if (photoList.item(l).getNodeName().equals("ImagePath")){
								stringBuilder.append(photoList.item(l).getTextContent().toLowerCase());   // 图片名称
							}
							if(photoList.item(l).getNodeName().equals("Pose")){
//                                System.out.println(photoList.item(l).getNodeName());    // Pose
								poseList = photoList.item(l).getChildNodes();
								for(int poseLen=0; poseLen<poseList.getLength();poseLen++){
									if(poseList.item(poseLen).getNodeName().equals("Metadata")){
										metadataList = poseList.item(poseLen).getChildNodes();
										for(int metadataItem=0; metadataItem< metadataList.getLength(); metadataItem++){
//                                            System.out.println(metadataList.item(centerItem).getNodeName());    //Center *n

											if(metadataList.item(metadataItem).getNodeName().equals("Center")){
												gpsInfoEntity = new GpsInfoEntity();
												centerList = metadataList.item(metadataItem).getChildNodes();
												for (int centerItem =0; centerItem< centerList.getLength();centerItem++){
													if(centerList.item(centerItem).getNodeName().equals("x")){
														gpsInfoEntity.setLongitude(Double.parseDouble(centerList.item(centerItem).getTextContent()));   // X
													}
													if(centerList.item(centerItem).getNodeName().equals("y")){
														gpsInfoEntity.setLatitude(Double.parseDouble(centerList.item(centerItem).getTextContent()));    // Y
													}
													if(centerList.item(centerItem).getNodeName().equals("z")){
														gpsInfoEntity.setAltitude((int)Math.round(Double.parseDouble(centerList.item(centerItem).getTextContent())));   // Z
													}
												}
											}

										}
									}
								}
							}
						}
						if(stringBuilder.length()>0){
							atXmlMap.put(stringBuilder.toString(),gpsInfoEntity);
						}
						stringBuilder.setLength(0);
					}
				}
			}
		}
		return atXmlMap;
	}

	/**
	 * 解析TXT获取内容保存为GpsInfoEntity实例, 用于JPG GPS定位写入
	 * @param txtFile
	 * @return Map<"文件名称",GpsInfoEntity>
	 */
	public static Map<String,GpsInfoEntity> parsePosTXT(File txtFile){
		String fileItem = "file".toLowerCase();
		String latitudeItem = "latitude".toLowerCase();
		String longitudeItem = "longitude".toLowerCase();
		String altitudeItem = "altitude".toLowerCase();

		Map<String, GpsInfoEntity> infoMap = new HashMap<>();
		Map<String, Integer> itemMap = new HashMap<>();

		ArrayList<String> arrayList = readFullDocument(txtFile.getPath());
		GpsInfoEntity gpsInfoEntity;
		String[] lineStr;
		for (String line: arrayList){
			if (itemMap.size() <=3){
				lineStr = line.trim().split("\\t+");
				if (lineStr.length <=3){
					lineStr = line.trim().split("\\s+");
				}
				if (lineStr.length >= 3){
					for (int i =0; i< lineStr.length; i++){
						itemMap.put(lineStr[i].trim().toLowerCase(), i);
					}
				}
				continue;
			}
			// 开始处理内容
			lineStr = line.split("\\s+");
			gpsInfoEntity = new GpsInfoEntity();
			gpsInfoEntity.setLatitude(Double.parseDouble(lineStr[itemMap.get(latitudeItem)]));
			gpsInfoEntity.setLongitude(Double.parseDouble(lineStr[itemMap.get(longitudeItem)]));
			gpsInfoEntity.setAltitude((int)Math.round(Double.parseDouble(lineStr[itemMap.get(altitudeItem)])));
			infoMap.put(FilenameUtils.getName(lineStr[itemMap.get(fileItem)]).toLowerCase(), gpsInfoEntity);
		}
//		System.err.println(infoMap.size());
		return infoMap;
	}



}
