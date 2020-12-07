package com.ambit.app.controller;

import com.ambit.app.process.ExecutorProcessPool;
import com.ambit.app.task.SendEmailTask;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SendEmail {

    // 创建线程池
    private ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

    @FXML
    private AnchorPane root;

    @FXML
    private TextField excelPathText;

    @FXML
    private TextArea statusWindow;

    @FXML
    private TextField subjectText;

    @FXML
    private TextField attachmentText;

    @FXML
    private Button startBtn;

    @FXML
    void browseExcelPath(ActionEvent event) {

    }

    @FXML
    void browseFiles(ActionEvent event) {

    }

    @FXML
    void startSend(ActionEvent event) {
        startBtn.setDisable(true);  //禁用按钮

        SendEmailTask sendEmailTask = new SendEmailTask();

        statusWindow.textProperty().bind(sendEmailTask.messageProperty());

        sendEmailTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED){
                startBtn.setDisable(false);
                statusWindow.textProperty().unbind();
            }
        }));
        pool.submit(sendEmailTask);
    }
}
