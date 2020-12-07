package com.ambit.app.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class JavaFXFileSelectTool {

	private static DirectoryChooser directoryChooser = new DirectoryChooser();

	/**
	 * 随时更新"最后一次选择的路径"
	 * @param path
	 */
	public static void setLastUsePath(String path){
//		if(null == System.getProperty("lastUsePath") || !System.getProperty("lastUsePath").equals(path)){
//			boolean flag = JavaFXTool.showConfirmWarnAlert(null,null,"设置为默认打开路径吗?",null);
//			if (flag){
				System.setProperty("lastUsePath",path);
//			}
//		}
	}
	/**
	 * 只允许选择格式为.log的文件
	 * @param fileChooser
	 */
	public static void configureLogFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("View Log File");
		fileChooser.setInitialDirectory(
            new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath"))
        );
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Log", "*.log")
        );
	}

	/**
	 * 选择任意文件
	 * @param fileChooser
	 */
	public static void configureFileChooser(final FileChooser fileChooser){
		fileChooser.setTitle("View File");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath"))
		);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("*", "*")
		);
	}
	/**
	 * 保存任意文件
	 * @param fileChooser
	 */
	public static void configureSaveFileChooser(final FileChooser fileChooser, String fileName){
		fileChooser.setTitle("Save File");
		fileChooser.setInitialFileName(fileName);
		fileChooser.setInitialDirectory(new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*","*"));
	}
	/**
	 * 只允许选择格式为.txt的文件
	 * @param fileChooser
	 */
	public static void configureTXTFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("View TXT File");
        fileChooser.setInitialDirectory(
			new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath"))
        );
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
	}
	/**
	 * 只允许选择.jpg & .png
	 * @param fileChooser
	 */
	public static void configurePhotoFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
        	new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath"))
		);
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
	}

	/**
	 * 只允许选择.json文件
	 * @param fileChooser
	 */
	public static void configureJsonFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("View Json File");
		fileChooser.setInitialDirectory(
			new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath"))

		);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JSON", "*.json")
		);
	}
	/**
	 *  只允许打开.Octagon文件
	 * @param fileChooser
	 */
	public static void configureOctagonFileChooser(final FileChooser fileChooser) {
		fileChooser.setInitialDirectory(
			new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath"))
		);
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("OCTAGON", "*.octagon")
		);
	}

	/**
	 * 只允许打开*.kml文件
	 * @param fileChooser
	 */
	public static void configureKMLFileChooser(final FileChooser fileChooser){
		fileChooser.setTitle("View KML File");
		fileChooser.setInitialDirectory(new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("KML/KMZ","*.kml","*.kmz"));
	}
	/**
	 * 只允许打开*.kml文件
	 * @param fileChooser
	 */
	public static void configureSaveKMLFileChooser(final FileChooser fileChooser, String kmlName){
		fileChooser.setTitle("Save KML File");
		fileChooser.setInitialFileName(kmlName);
		fileChooser.setInitialDirectory(new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("KML","*.kml"));
	}

	/**
	 * 调出文件夹选择器窗口
	 * @return
	 */
	public static DirectoryChooser showDirectoryChooser(){
		directoryChooser.setTitle("Please Select Folder");
		directoryChooser.setInitialDirectory(new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath")));
		return directoryChooser;
	}

	/**
	 * 保存json格式文件
	 */
	public static void configureSaveJsonFileChooser(final FileChooser fileChooser, String projectName){
		fileChooser.setTitle("Save Project File");
		fileChooser.setInitialFileName(projectName);
		fileChooser.setInitialDirectory(new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath")));
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JSON", "*.json")
		);
	}

	/**
	 * 保存.Octagon格式文件
	 * @param fileChooser
	 * @param projectName
	 */
	public static void configureSaveOctagonFileChooser(final FileChooser fileChooser, String projectName){
		fileChooser.setTitle("Save Project File");
		fileChooser.setInitialFileName(projectName);
		fileChooser.setInitialDirectory(new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath")));
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("OCTAGON", "*.octagon")
		);
	}
	/**
	 * 只允许选择.json文件
	 * @param fileChooser
	 */
	public static void configureXMLFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("View XML File");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("lastUsePath") == null ? System.getProperty("user.home") : System.getProperty("lastUsePath"))

		);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML", "*.xml")
		);
	}

	public static File selectedOpenTXTFile(Window root){
		FileChooser fileChooser = new FileChooser();
		configureTXTFileChooser(fileChooser);
		File txtFile = fileChooser.showOpenDialog(root.getScene().getWindow());
		if (null == txtFile) { return null; }
		setLastUsePath(txtFile.getParent());
		return txtFile;
	}
	public static File selectedOpenKMLFile(Window root){
		FileChooser kmlFileChooser = new FileChooser();
		JavaFXFileSelectTool.configureKMLFileChooser(kmlFileChooser);
		File kmlFile = kmlFileChooser.showOpenDialog(root.getScene().getWindow());
		if (null == kmlFile){return null; }
		JavaFXFileSelectTool.setLastUsePath(kmlFile.getParent());
		return kmlFile;
	}

	/**
	 * 直接调用选取XML文件的窗口
	 * @param root
	 * @return
	 */
	public static File selectedOpenXMLFile(Window root){
		FileChooser fileChooser = new FileChooser();
		configureXMLFileChooser(fileChooser);
		File xmlFile = fileChooser.showOpenDialog(root.getScene().getWindow());
		if (null == xmlFile) { return null; }
		setLastUsePath(xmlFile.getParent());
		return xmlFile;
	}
	public static File selectedSaveKMLFile(Window root, String kmlName){
		FileChooser saveFileChooser = new FileChooser();
		configureSaveKMLFileChooser(saveFileChooser,kmlName);
		File savePath = saveFileChooser.showSaveDialog(root);
		if (null == savePath) {return null;}
		setLastUsePath(savePath.getParent());
		return savePath;
	}

	/**
	 * 选择任意文件
	 * @param root
	 * @return
	 */
	public static File selectedFile(Window root){
		FileChooser fileChooser = new FileChooser();
		configureFileChooser(fileChooser);
		File file = fileChooser.showOpenDialog(root.getScene().getWindow());
		if (null == file) { return null; }
		setLastUsePath(file.getParent());
		return file;
	}

	/**
	 * 保存任意文件
	 * @param root
	 * @param fileName
	 * @return
	 */
	public static File selectedSaveFile(Window root, String fileName){
		FileChooser saveFileChooser = new FileChooser();
		configureSaveFileChooser(saveFileChooser, fileName);
		File savePath = saveFileChooser.showSaveDialog(root);
		if (null == savePath) {return null;}
		setLastUsePath(savePath.getParent());
		return savePath;
	}

}
