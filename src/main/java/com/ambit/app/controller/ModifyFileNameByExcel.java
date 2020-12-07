package com.ambit.app.controller;

import com.ambit.app.utils.JavaFXFileSelectTool;
import com.ambit.app.utils.POITool;
import com.ambit.app.utils.ParseDocumentTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * 根据Excel内容修改文件名称
 */
public class ModifyFileNameByExcel {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField sourceFolderText;

    @FXML
    private TextField excelFileText;

    @FXML
    private Text statusText;

    @FXML
    private TextArea log;

    @FXML
    void browseExcelPath(ActionEvent event) {
        File selectedOpenXMLFile = JavaFXFileSelectTool.selectedFile(root.getScene().getWindow());
        if (null == selectedOpenXMLFile){
            return;
        }
        excelFileText.setText(selectedOpenXMLFile.getPath());
        event.consume();
    }

    @FXML
    void browseSourcePath(ActionEvent event) {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File sourcePath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == sourcePath){return;}
        JavaFXFileSelectTool.setLastUsePath(sourcePath.getPath());
        sourceFolderText.setText(sourcePath.getPath());
        event.consume();
    }

    @FXML
    void start(ActionEvent event) {
        // 1读取源数据
        List<File> sourceList = new ArrayList<>();
        ParseDocumentTool.findAllFileByFolderPath(new File(sourceFolderText.getText()),sourceList);

        // 2读取Excel文档
        String columns[] = {"old Name","new Name"};
        List<Map<String, String>> list = new POITool().readExcel(excelFileText.getText(), columns);
        //遍历解析出来的list
        Map<String,String> allValue = new HashMap<>();
        for (Map<String,String> map : list) {
            allValue.put(map.get("old Name"),map.get("new Name"));
        }

        // 3按文件名称匹配
        File newFileName;
        String temp;
        for (File f: sourceList){
            temp = allValue.get(FilenameUtils.getBaseName(f.getName()));
            if (null != temp){
                temp = temp.concat("."+FilenameUtils.getExtension(f.getName()));
                newFileName = new File(f.getParentFile(),temp);
                if (!f.renameTo(newFileName)){
                    log.appendText("修改失败: ["+f.getPath()+"]");
                }
            }
        }
        statusText.setText(new Date()+" - 执行完毕");

    }
}
