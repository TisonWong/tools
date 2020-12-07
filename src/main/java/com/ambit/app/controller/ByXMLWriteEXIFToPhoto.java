package com.ambit.app.controller;

import com.ambit.app.process.ExecutorProcessPool;
import com.ambit.app.task.ChangeEXIFTask;
import com.ambit.app.utils.JavaFXFileSelectTool;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * 解析XML中的坐标信息, 写入到相应照片中
 */
public class ByXMLWriteEXIFToPhoto {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField xml_text;

    @FXML
    private Button start_btn;

    @FXML
    private TextField photo_text;

    @FXML
    private ProgressBar progressBarId;

    @FXML
    private TextArea statusBar;

    @FXML
    private TextField exportText;

    @FXML
    private Button exportBtn;

    @FXML
    private TextField threadCountText;


    @FXML
    private CheckBox startMultithreadingCheckBox;

    // 创建线程池
    private ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

    @FXML
    void selectPhotoFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File photoPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == photoPath){return;}
        JavaFXFileSelectTool.setLastUsePath(photoPath.getPath());
        photo_text.setText(photoPath.getPath());
        event.consume();
    }
    @FXML
    void selectExportFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File photoPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == photoPath){return;}
        JavaFXFileSelectTool.setLastUsePath(photoPath.getPath());
        exportText.setText(photoPath.getPath());
        event.consume();
    }

    @FXML
    void selectXmlFile(ActionEvent event) {
        File selectedOpenXMLFile = JavaFXFileSelectTool.selectedFile(root.getScene().getWindow());
        if (null == selectedOpenXMLFile){
            return;
        }
        xml_text.setText(selectedOpenXMLFile.getPath());
        event.consume();
    }

    @FXML
    void start(ActionEvent event) {

        if ("".equals(xml_text.getText()) && "".equals(photo_text.getText()) && "".equals(exportText.getText())){
            new Alert(Alert.AlertType.WARNING, "请填充完整参数").show();
            return;}

        ChangeEXIFTask changeEXIFTask = null;
        if (startMultithreadingCheckBox.isSelected()){
            int threadCount = 10;
            if (!"".equals(threadCountText.getText())) {
                try {
                    threadCount = Integer.parseInt(threadCountText.getText());
                }catch (NumberFormatException e){
                    new Alert(Alert.AlertType.WARNING,"请输入正确的线程数").show();
                    return;
                }
            }
            changeEXIFTask = new ChangeEXIFTask(xml_text.getText(), photo_text.getText(), exportText.getText(), true, threadCount);
        }else{
            changeEXIFTask = new ChangeEXIFTask(xml_text.getText(), photo_text.getText(), exportText.getText());
        }
        start_btn.setDisable(true);

        progressBarId.progressProperty().bind(changeEXIFTask.progressProperty());
        statusBar.textProperty().bind(changeEXIFTask.messageProperty());

        pool.execute(changeEXIFTask);

        // 监听任务执行情况
        changeEXIFTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED){
                start_btn.setDisable(false);
                progressBarId.progressProperty().unbind();
                statusBar.textProperty().unbind();
            }
        }));

        event.consume();
    }

}
