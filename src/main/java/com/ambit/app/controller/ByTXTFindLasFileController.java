/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */

package com.ambit.app.controller;

import com.ambit.app.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ByTXTFindLasFileController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // fx:id="Log_Column"
    private TextArea Log_Column; // Value injected by FXMLLoader

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="TXT_Folder"
    private TextField TXT_Folder; // Value injected by FXMLLoader

    @FXML // fx:id="Las_Folder"
    private TextField Las_Folder; // Value injected by FXMLLoader

    @FXML // fx:id="Output_Folder"
    private TextField Output_Folder; // Value injected by FXMLLoader

    Map<String, ArrayList<String>> txtMap = new HashMap<>();

    @FXML
    void ParsingTXTDocument(ActionEvent event) {
        txtMap.clear();
        String txtFolder = TXT_Folder.getText();
        if(txtFolder!=null && !txtFolder.equals("")){
            File txtFolderFile = new File(txtFolder);
            if(txtFolderFile.exists()){
                txtMap = ParseDocumentTool.readFullTXTDocument(txtFolderFile,txtMap);
//                Iterator<Map.Entry<String,ArrayList<String>>> iterator = txtMap.entrySet().iterator();
//                while (iterator.hasNext()){
//                    Map.Entry<String, ArrayList<String>> map = iterator.next();
//                    System.out.println(map.getKey()+","+map.getValue().toString());
//                }
            }else{
                Log_Column.appendText("\nTXT目录不存在");
            }
        }else{
            Log_Column.appendText("\n请输入TXT文档所在目录");
        }
    }

    @FXML
    void CopyLasFiles(ActionEvent event) {
        String lasFolder = Las_Folder.getText();
        // 输出目录路径
        String outPutFolder = Output_Folder.getText();
        if(lasFolder!=null && !lasFolder.equals("")){
            if(outPutFolder==null || outPutFolder.equals("")){
                Log_Column.appendText("\n请输入输出目录");
                return;
            }
            // 转换Las目录为映射对象
            File lasFolderFile = new File(lasFolder);
            if(lasFolderFile.exists()){
                if(txtMap.size()<1){
                    Log_Column.appendText("\n请先解析TXT文档");
                    return;
                }
                Map<String,File> lasMap = new HashMap<>();

                // Java FX子线程执行，不会导致主线程阻塞
                Platform.runLater(()->{

                });

                // 开始读取Las目录
                lasMap = ParseDocumentTool.getAllFiles(lasFolderFile,lasMap, "las");

                // 如果目录下找不到任何文件则结束执行
                if(lasMap.size()<1){
                    Log_Column.appendText("\n文件夹下找不到任何文件");
                    return;
                }

                try{
                    File tempFile;
                    StringBuilder fileSB = new StringBuilder();
                    // 开始执行匹配和文件复制
                    for(Map.Entry<String, ArrayList<String>> txtEntry: txtMap.entrySet()){
                        Log_Column.appendText("\n---------"+txtEntry.getKey()+"\n");
                        for(String fileName:txtEntry.getValue()){
                            tempFile = lasMap.get(fileName);

                            if(tempFile!=null && tempFile.exists()){
                                fileSB.setLength(0);
                                fileSB.append(outPutFolder).append(File.separator).append(tempFile.getName());
                                FileOperationUtil.copyFileUsingFileStreams(tempFile,new File(fileSB.toString()));
                                Log_Column.appendText(tempFile.getPath()+" [Copy To] "+fileSB.toString()+"\n");
                            }
                        }
                    }

                }catch (IOException e){
                    Log_Column.appendText("复制失败："+e.getMessage());
                }


            }else{
                Log_Column.appendText("\nLas目录不存在");
            }
        }else{
            Log_Column.appendText("\n请输入Las文件所在目录");
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        
    }
}
