package com.ambit.app.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * 主窗口管理器
 */
public class MainWindow implements Initializable {

    @FXML
    private ListView<String> tool_listView;

    @FXML
    private TabPane tool_window;

    private Map<String, String> toolMap = new HashMap<>();

    // 已创建的Tab实例集合
    Map<String,Tab> tabMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 将工具渲染到列表上
        String byTXTFindLasFile = "/view/ByTXTFindLasFileStage.fxml";
        toolMap.put(FilenameUtils.getBaseName(byTXTFindLasFile),byTXTFindLasFile);
        String byTXTCopyOrMovePhoto = "/view/ByTXTCopyOrMovePhoto.fxml";
        toolMap.put("根据TXT内容找到照片进行复制或移动",byTXTCopyOrMovePhoto);
        String byXMLWriteEXIFToPhoto = "/view/ByXMLWriteEXIFToPhoto.fxml";
        toolMap.put("根据XML内容查找照片修改其EXIF参数",byXMLWriteEXIFToPhoto);
        String fileChunkAndMerge = "/view/FileChunkAndMerge.fxml";
        toolMap.put("文件分块与合并",fileChunkAndMerge);
        String multithreadingDemo = "/view/MultithreadingDemo.fxml";
        toolMap.put("多线程工具",multithreadingDemo);
        String getsTheEXIFOfTheJPG = "/view/GetsTheEXIFOfTheJPG.fxml";
        toolMap.put("获取照片EXIF数据",getsTheEXIFOfTheJPG);
        String findAndCopyFiles = "/view/FindAndCopyFiles.fxml";
        toolMap.put("根据文档内容查找及复制文件",findAndCopyFiles);
        String modifyFileNameByExcel = "/view/ModifyFileNameByExcel.fxml";
        toolMap.put("根据Excel内容修改文件名称",modifyFileNameByExcel);
        String change3DSFileName = "/view/Change3DSFileName.fxml";
        toolMap.put("修改3DS模型的图片名称以及分类",change3DSFileName);
        String sendEmail = "/view/SendEmail.fxml";
        toolMap.put("读取Excel列表发送邮件",sendEmail);



        // 监听工具被选中
        tool_listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue){
                Tab selectedTab;
                if(null != (selectedTab = tabMap.get(FilenameUtils.getBaseName(toolMap.get(newValue))))){
                    tool_window.getSelectionModel().select(selectedTab);
                    return;
                }
                try {
                    openTool(toolMap.get(newValue));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        tool_listView.setOnMouseClicked(event -> {
//            单击判断 event.getButton()==MouseButton.PRIMARY
//            右击判断 event.getButton()==MouseButton.SECONDARY
//            双击判断 event.getClickCount() == 2
           if (event.getClickCount()==2){
               String selectedItem = tool_listView.getSelectionModel().getSelectedItem();
               if (null != selectedItem){
                   try {
                       openTool(toolMap.get(selectedItem));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
        });

        // 渲染工具列表
        for (String key :toolMap.keySet()){
            tool_listView.getItems().add(key);
        }

    }

    /**
     * 调用工具
     * @param fxPath
     * @throws IOException
     */
    private void openTool(String fxPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource(fxPath));
        AnchorPane anchorPane = fxmlLoader.load();
        Tab tab = new Tab(FilenameUtils.getBaseName(fxPath));
        tab.setContent(anchorPane);
        String baseName = FilenameUtils.getBaseName(fxPath);
        if (null != tabMap.get(baseName)){
            baseName = baseName + Math.random();
        }
        tab.setId(baseName);
        tabMap.put(tab.getId(),tab);
        tool_window.getTabs().add(tab);
    }



}
