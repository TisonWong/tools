package com.ambit.app.controller;

import com.ambit.app.process.ExecutorProcessPool;
import com.ambit.app.task.CopyPhotoTask;
import com.ambit.app.task.MovePhotoTask;
import com.ambit.app.utils.JavaFXFileSelectTool;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 根据TXT文档内容, 查找相片执行复制或移动
 */
public class ByTXTCopyOrMovePhoto implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private TextArea state_show;

    @FXML
    private TextField import_file;

    @FXML
    private TextField export_folder;

    @FXML
    private TextField import_folder;

    @FXML
    private Button startMoveBtn;

    @FXML
    private Button startCopyBtn;

    @FXML
    private ProgressBar loading;

    // 创建线程池
    private ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

    @FXML
    void browseExportPath(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File exportPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == exportPath){return;}
        JavaFXFileSelectTool.setLastUsePath(exportPath.getPath());
        if(event.getTarget().toString().contains("资源")){
            import_folder.setText(exportPath.getPath());
        }else{
            export_folder.setText(exportPath.getPath());
        }
    }

    @FXML
    void browseTXTPath(ActionEvent event) {
        File txtFile = JavaFXFileSelectTool.selectedOpenTXTFile(root.getScene().getWindow());
        if (null == txtFile){
            return;
        }
        import_file.setText(txtFile.getPath());
    }

    @FXML
    void startCopy(ActionEvent event) {
        if ("".equals(import_file.getText()) || "".equals(import_folder.getText()) || "".equals(export_folder.getText())){
            new Alert(Alert.AlertType.WARNING, "请填充完整的参数").show();
            return;}

        // 防止重复触发
        startMoveBtn.setDisable(true);
        startCopyBtn.setDisable(true);

        CopyPhotoTask copyPhotoTask = new CopyPhotoTask(import_file.getText(), import_folder.getText(), export_folder.getText());

        // 绑定进度条
        loading.progressProperty().bind(copyPhotoTask.progressProperty());
        // 绑定日志窗口
        state_show.textProperty().bind(copyPhotoTask.messageProperty());

        pool.submit(copyPhotoTask);

        // 监听任务执行情况
        copyPhotoTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED){
                startMoveBtn.setDisable(false);
                startCopyBtn.setDisable(false);

                loading.progressProperty().unbind();
                state_show.textProperty().unbind();
            }
        }));
        event.consume();
    }

    @FXML
    void startMove(ActionEvent event) {
        if ("".equals(import_file.getText()) || "".equals(import_folder.getText()) || "".equals(export_folder.getText())){
            new Alert(Alert.AlertType.WARNING, "请填充完整的参数").show();
            return;}

        // 防止重复触发
        startMoveBtn.setDisable(true);
        startCopyBtn.setDisable(true);

        MovePhotoTask movePhotoTask = new MovePhotoTask(import_file.getText(), import_folder.getText(), export_folder.getText());

        // 绑定进度条
        loading.progressProperty().bind(movePhotoTask.progressProperty());
        // 绑定日志窗口
        state_show.textProperty().bind(movePhotoTask.messageProperty());

        pool.submit(movePhotoTask);

        // 监听任务执行情况
        movePhotoTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED){
                startMoveBtn.setDisable(false);
                startCopyBtn.setDisable(false);

                loading.progressProperty().unbind();
                state_show.textProperty().unbind();
            }
        }));
        event.consume();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        addOnClose();
    }

//    private void addOnClose(){
//        Stage stage = (Stage)root.getScene().getWindow();
//        stage.onCloseRequestProperty().addListener((observable)->{
//            // 窗口关闭时关闭线程池
//            pool.shutdown();
//        });
//    }

}
