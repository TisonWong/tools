package com.ambit.app.controller;

import com.ambit.app.process.ExecutorServiceFactory;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class MultithreadingDemo implements Initializable {

    @FXML
    private ScrollPane scrollPaneRoot;

    @FXML
    private VBox vBoxRoot;

    @FXML
    private TextField threadStatusText;

    @FXML
    private TextField threadCountText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 启动监听线程
        ListenThreadTask threadTask = new ListenThreadTask(executorService);
        executorService.execute(threadTask);
        threadStatusText.textProperty().bind(threadTask.messageProperty());
    }

    // 线程池
    ExecutorServiceFactory executorServiceFactory = ExecutorServiceFactory.getInstance();
    ExecutorService executorService = executorServiceFactory.createFixedThreadPool(10);

    @FXML
    void start(ActionEvent event) {
//        int threadCount = Integer.parseInt(threadCountText.getText());

        for (int i=0; i < 8; i++){
            ProgressBar progressBar = new ProgressBar();
            progressBar.setId(i+"P");
            progressBar.setPrefSize(200,20);

            Label label = new Label();
            label.setId(i+"L");
            label.setGraphic(progressBar);
            label.setContentDisplay(ContentDisplay.LEFT);

            MultithreadingTask multithreadingTask = new MultithreadingTask();

            progressBar.progressProperty().bind(multithreadingTask.progressProperty());
            label.textProperty().bind(multithreadingTask.messageProperty());
            vBoxRoot.getChildren().addAll(progressBar,label);

            if (!executorService.isShutdown()){
                executorService.execute(multithreadingTask);
            }

            multithreadingTask.stateProperty().addListener((observable, oldValue, newValue) -> {
                System.err.println(newValue);
                if (newValue == Worker.State.SUCCEEDED){
                    vBoxRoot.getChildren().removeAll(label, progressBar);
                }
            });
        }
    }
}

class ListenThreadTask extends Task<Boolean>{
    ExecutorService executorService;
    public ListenThreadTask(ExecutorService executorService){
        this.executorService = executorService;
    }

    @Override
    protected Boolean call() throws Exception {
        // 监听线程执行进度
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
        while(true){
            int queueSize = threadPoolExecutor.getQueue().size();
            int activeCount = threadPoolExecutor.getActiveCount();
            long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
            long taskCount = threadPoolExecutor.getTaskCount();
            updateMessage("当前排队线程数:"+queueSize+"\t当前活动线程数:"+activeCount+"\t执行完成线程数:"+completedTaskCount+"\t总线程数:"+taskCount);
//            if (activeCount<1){break;}
        }
//        return true;
    }
}

class MultithreadingTask extends Task<Boolean> {
    int count = 5;
    @Override
    protected Boolean call() throws Exception {
        updateMessage("执行中>>>>");
        for (int i=0; i < count; i++){
            Thread.sleep(1000);
            updateProgress((i+1), count);
        }
        updateMessage("执行完毕");
        return true;
    }
}