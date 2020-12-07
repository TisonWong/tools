package com.ambit.app.controller;

import com.ambit.app.process.ExecutorProcessPool;
import com.ambit.app.utils.JavaFXFileSelectTool;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.*;

public class FileChunkAndMerge implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField chunkSourceFileText;

    @FXML
    private TextField chunkOutputText;

    @FXML
    private TextField chunkFileSizeText;

    @FXML
    private TextField mergeSourceFileText;

    @FXML
    private TextField mergeOutputText;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressMsg;


    @FXML
    private Button startChunkBtn;

    @FXML
    private Button startMergeBtn;

    // 创建线程池
    private ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

    @FXML
    void selectFile(ActionEvent event) {
        File file = JavaFXFileSelectTool.selectedFile(root.getScene().getWindow());
        if (null == file){
            return;
        }
        chunkSourceFileText.setText(file.getPath());
    }

    @FXML
    void selectPath(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File exportPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == exportPath){return;}
        JavaFXFileSelectTool.setLastUsePath(exportPath.getPath());
        if(event.getTarget().toString().contains("分块导出目录")){
            chunkOutputText.setText(exportPath.getPath());
        }else if(event.getTarget().toString().contains("合并源文件")){
            mergeSourceFileText.setText(exportPath.getPath());
        }
    }

    @FXML
    void selectSaveFile(ActionEvent event){
        if ("".equals(mergeSourceFileText.getText())){
            new Alert(Alert.AlertType.WARNING, "先选择源文件路径").show();
            return;
        }
        File saveFile = JavaFXFileSelectTool.selectedSaveFile(root.getScene().getWindow(), FilenameUtils.getName(mergeSourceFileText.getText()));
        if (null == saveFile){
            return;
        }
        mergeOutputText.setText(saveFile.getPath());
    }

    /**
     * 开始分块
     * @param event
     */
    @FXML
    void startChunk(ActionEvent event) {
        if("".equals(chunkSourceFileText.getText()) || "".equals(chunkOutputText.getText())){
            new Alert(Alert.AlertType.WARNING,"请填充完整信息").show();
            return;
        }
        // 源文件
        File sourceFile = new File(chunkSourceFileText.getText());
        // 导出目录
        File outputFolder = new File(chunkOutputText.getText());
        // 分块大小
        long chunkFileSize = Integer.parseInt(chunkFileSizeText.getText());

        Task<Boolean> chunkTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
            // 兆字节转比特  (比特/1024)= 千字节/1024 = 兆字节
            long chunkFileSizeToByte =  ((chunkFileSize*1024)*1024);
            // 定义块文件大小
            long chunkFileNum = (long)Math.ceil(sourceFile.length() * 1.0 / chunkFileSizeToByte);// 四舍五入long chunkFileNum = 4;
            // 读取文件对象
            RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");

            byte[] b = new byte[1024];

            // 块文件对象
            File chunkFile = null;
            int len = -1;
            // 写入文件对象
            RandomAccessFile raf_write = null;
            String fileInfo = "\t [文件大小:"+((sourceFile.length()/1024)/1024)+"字节,共分成:"+chunkFileNum+"块]";
            // 拼接导出路径 : 导出路径 + 源文件名称 + 块文件...*n
            String outputPath = outputFolder.getPath()+File.separator+ FilenameUtils.getBaseName(sourceFile.getName())+ File.separator;
            for(int i =0; i< chunkFileNum; i++){
                updateProgress((i+1), chunkFileNum);
                chunkFile = new File(outputPath, String.valueOf(i));
                if (!chunkFile.getParentFile().exists()){
                    chunkFile.getParentFile().mkdirs();
                }
                updateMessage("正在分块: "+(i+1)+"/"+chunkFileNum + fileInfo);
                raf_write = new RandomAccessFile(chunkFile,"rw");
                while ((len = raf_read.read(b))!=-1){
                    raf_write.write(b,0,len);
                    if(chunkFile.length() >= chunkFileSizeToByte){
                        break;
                    }
                }
                raf_write.close();
            }

            raf_read.close();
            updateMessage(sourceFile.getName() + "[分块完毕]");
            return true;
            }
        };
        startChunkBtn.setDisable(true);
        progressBar.progressProperty().bind(chunkTask.progressProperty());
        progressMsg.textProperty().bind(chunkTask.messageProperty());

        pool.execute(chunkTask);

        chunkTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED){
                startChunkBtn.setDisable(false);
                progressBar.progressProperty().unbind();
                progressMsg.textProperty().unbind();
            }
        }));

    }

    /**
     * 开始合并
     * @param event
     */
    @FXML
    void startMerge(ActionEvent event) {
        if("".equals(mergeSourceFileText.getText()) || "".equals(mergeOutputText.getText())){
            new Alert(Alert.AlertType.WARNING,"请填充完整信息").show();
            return;
        }
        // 源文件目录
        File sourceFolder = new File(mergeSourceFileText.getText());
        // 输出目录
        File outputFile = new File(mergeOutputText.getText());

        Task<Boolean> mergeTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                File[] files = sourceFolder.listFiles();

                List<File> fileList = Arrays.asList(files);
                Collections.sort(fileList, new Comparator<File>() {
                    public int compare(File o1, File o2) {
                        if((Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName()))){
                            return 1;
                        }
                        return -1;
                    }
                });

                boolean newFileFlag = outputFile.createNewFile();
                if (newFileFlag){
                    updateMessage(outputFile.getPath()+" 文件已存在");
                }
                RandomAccessFile raf_write = new RandomAccessFile(outputFile,"rw");

                RandomAccessFile raf_read = null;
                int n=0;
                int len = -1;
                byte[] b = new byte[1024];
                long fileSize = (fileList.size()+1);
                for(File chunkFile : fileList){
                    n++;
                    updateProgress((n+1), fileSize);
                    updateMessage("文件合并中: "+n+"/"+ fileSize);
                    raf_read = new RandomAccessFile(chunkFile,"r");
                    while ((len = raf_read.read(b))!=-1){
                        raf_write.write(b,0,len);
                    }
                    raf_read.close();
                }
                raf_write.close();
                updateMessage(outputFile.getName() + "[合并完毕]");
                return true;
            }
        };

        startMergeBtn.setDisable(true);
        progressBar.progressProperty().bind(mergeTask.progressProperty());
        progressMsg.textProperty().bind(mergeTask.messageProperty());

        pool.execute(mergeTask);

        mergeTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED){
                startMergeBtn.setDisable(false);
                progressBar.progressProperty().unbind();
                progressMsg.textProperty().unbind();
            }
        }));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
