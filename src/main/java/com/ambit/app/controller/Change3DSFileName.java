package com.ambit.app.controller;

import com.ambit.app.process.ExecutorProcessPool;
import com.ambit.app.utils.FileOperationUtil;
import com.ambit.app.utils.JavaFXFileSelectTool;
import com.ambit.app.utils.ParseDocumentTool;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.*;

/**
 * 批量修改文件名称
 * 事件:
 *  从LandsD获取的3DS模型文件名称过长 >8
 *  而3DS转换3DML工具仅识别 == 8 的源文件
 *  因此需要将所有涉及到需要转换的3DS & JPG文件重新命名, 将大于 8 位以后的字符去除
 *
 */
public class Change3DSFileName {
    @FXML
    private AnchorPane root;

    @FXML
    private TextArea statusText;

    @FXML
    private TextField sourceFolderText;
    @FXML
    private Button startBtn;
    @FXML
    private TextField outPutMappingModelText;

    @FXML
    private TextField outPutSingleModelText;

    @FXML
    private TextField nameIndexText;

    // 创建线程池
    private ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

    @FXML
    void browseMappingExportPath(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File exportPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == exportPath){return;}
        JavaFXFileSelectTool.setLastUsePath(exportPath.getPath());
        outPutMappingModelText.setText(exportPath.getPath());
    }

    @FXML
    void browseSingleExportPath(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File exportPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == exportPath){return;}
        JavaFXFileSelectTool.setLastUsePath(exportPath.getPath());
        outPutSingleModelText.setText(exportPath.getPath());
    }

    @FXML
    void browseSourcePath(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File exportPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == exportPath){return;}
        JavaFXFileSelectTool.setLastUsePath(exportPath.getPath());
        sourceFolderText.setText(exportPath.getPath());
    }
    @FXML
    void start(ActionEvent event) {
        File sourceFolder = new File(sourceFolderText.getText());
        File outPutSingleModel = new File(outPutSingleModelText.getText());
        File outPutMappingModel = new File(outPutMappingModelText.getText());
        Integer nameIndex = Integer.parseInt(nameIndexText.getText());
        if (!sourceFolder.exists() || !outPutSingleModel.exists() || !outPutMappingModel.exists()){
            new Alert(Alert.AlertType.WARNING,"选取的文件路径不存在,请重新选择").show();
            return;
        }
        Find3DSFilesTask find3DSFilesTask = new Find3DSFilesTask(sourceFolder,outPutMappingModel,outPutSingleModel,nameIndex);
        pool.submit(find3DSFilesTask);
        statusText.textProperty().bind(find3DSFilesTask.messageProperty());
        startBtn.setDisable(true);  //禁用启动按钮
        // 监听任务执行情况
        find3DSFilesTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED){
                startBtn.setDisable(false);
                statusText.textProperty().unbind();
                statusText.appendText("\n"+new Date()+ ": 执行完毕");
            }
        }));
    }

}


class Find3DSFilesTask extends Task<Boolean>{

    private File sourceFolder;
    private File outPutSingleModel;
    private File outPutMappingModel;
    private Integer nameIndex;

    public Find3DSFilesTask(File sourceFolder,File outPutMappingModel,File outPutSingleModel,Integer nameIndex){
        this.sourceFolder = sourceFolder;
        this.outPutMappingModel = outPutMappingModel;
        this.outPutSingleModel = outPutSingleModel;
        this.nameIndex = nameIndex;
    }

    @Override
    protected Boolean call(){

        // 遍历目标文件夹
        List<File> allFiles = new ArrayList<>();
        Map<String,File> folderMap = new HashMap<>();

        ParseDocumentTool.findAllFileByFolderPath(sourceFolder,allFiles);
        if (allFiles.size()<1){
            updateMessage("没找到任何文件");
        }
        // 获取所有文件的父级文件夹名称
        for (File sourFile: allFiles){
            folderMap.put(sourFile.getParentFile().getName(),sourFile.getParentFile());
        }
        // 从Map集合中的父级文件夹查找JPG
        File targetFile;
        for (Map.Entry<String,File> fileEntry: folderMap.entrySet()){
            // 判断有无贴图, true: 无贴图; false: 有贴图
            if (isSingle(fileEntry.getValue())){
                for (File f: fileEntry.getValue().listFiles()){
                    updateMessage("正在复制:" +f.getName());
                    targetFile = new File(outPutSingleModel.getPath().concat(File.separator).concat(f.getParentFile().getName()),f.getName());
                    FileOperationUtil.copyFileUsingFileChannels(f,targetFile);
                }
            }else {
                for (File f: fileEntry.getValue().listFiles()){
                    updateMessage("正在复制:" +f.getName());
                    if (f.getName().toLowerCase().endsWith(".jpg")){
                        targetFile = new File(outPutMappingModel.getPath().concat(File.separator).concat(f.getParentFile().getName()),f.getName().substring(0,nameIndex) + ".JPG");
                    }else{
                        targetFile = new File(outPutMappingModel.getPath().concat(File.separator).concat(f.getParentFile().getName()),f.getName());
                    }
                    FileOperationUtil.copyFileUsingFileChannels(f,targetFile);
                }
            }
        }

        return true;
    }

    /**
     * 判断模型是不是无贴图
     * @param folder
     * @return
     */
    private boolean isSingle(File folder){
        for (File singleFile : folder.listFiles()){
            if (singleFile.getName().toLowerCase().endsWith(".jpg")){
                return false;
            }
        }
        return true;
    }
}