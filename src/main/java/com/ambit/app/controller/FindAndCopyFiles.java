package com.ambit.app.controller;

import com.ambit.app.utils.FileOperationUtil;
import com.ambit.app.utils.JavaFXFileSelectTool;
import com.ambit.app.utils.ParseDocumentTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 根据文档内容,查找文件,并复制到指定目录
 */
public class FindAndCopyFiles {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField importFolder;

    @FXML
    private TextField outputText;

    @FXML
    private TextField sourceFolderText;

    @FXML
    void selectFolder1(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File photoPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == photoPath){return;}
        JavaFXFileSelectTool.setLastUsePath(photoPath.getPath());
        importFolder.setText(photoPath.getPath());
        event.consume();
    }

    @FXML
    void selectFolder2(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File photoPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == photoPath){return;}
        JavaFXFileSelectTool.setLastUsePath(photoPath.getPath());
        sourceFolderText.setText(photoPath.getPath());
        event.consume();
    }

    @FXML
    void selectFolder3(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File photoPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == photoPath){return;}
        JavaFXFileSelectTool.setLastUsePath(photoPath.getPath());
        outputText.setText(photoPath.getPath());
        event.consume();
    }

    @FXML
    void start(ActionEvent event) {

        String importFolderText = importFolder.getText();
        String outputTextText = outputText.getText();
        String sourceFolderTextText = sourceFolderText.getText();
        if (null==importFolderText){
            new Alert(Alert.AlertType.WARNING,"先选择文档所在目录").show();
            return;
        }
        if (null==sourceFolderTextText){
            new Alert(Alert.AlertType.WARNING,"先选择源文件目录").show();
            return;
        }
        if (null == outputTextText){
            new Alert(Alert.AlertType.WARNING,"先选择导出目录").show();
            return;
        }

        // 读取文档内容
        File importFile = new File(importFolderText);
        Map<String, ArrayList<String>> arrayListMap = new HashMap<>();
        ParseDocumentTool.readFullTXTDocument(importFile, arrayListMap);

        // 读取目标目录下所有文件
        File sourceFolder = new File(sourceFolderTextText);
        Map<String, File> sourceFilesMap = new HashMap<>();
        ParseDocumentTool.getAllFiles(sourceFolder,sourceFilesMap,"*");
        File findFile;
        File outputFolder = null;
        File newFile;
        for (Map.Entry<String, ArrayList<String>> docEntrySet : arrayListMap.entrySet()){
            System.err.println(docEntrySet.getKey());
            // 遍历从文档中获取到的内容
            for (String fileName:docEntrySet.getValue()){
                findFile = sourceFilesMap.get(fileName);
                if (null != findFile){
                    // 创建输出文件夹
                    outputFolder = new File(outputTextText,docEntrySet.getKey());
                    if (!outputFolder.exists()){outputFolder.mkdirs();}
                    // 复制文件
                    newFile = new File(outputFolder,findFile.getName());
                    FileOperationUtil.copyFileUsingFileChannels(findFile,newFile);
                    System.out.println(findFile.getPath()+"\t→\t"+newFile.getPath());
                }
            }
        }
    }



}
