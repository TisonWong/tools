package com.ambit.app.task;

import com.ambit.app.utils.FileOperationUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ambit.app.utils.ParseDocumentTool;
import com.ambit.app.utils.ParsePhotoTool;
import org.apache.commons.io.FilenameUtils;

/**
 * 执行文件移动任务
 */
public class MovePhotoTask extends Task<Boolean> {

    private String txtFile, sourcePath, exportPath;
    private Integer notFind = 0;

    public MovePhotoTask(){}

    public MovePhotoTask(String txtFile, String sourcePath, String exportPath) {
        this.txtFile = txtFile;
        this.sourcePath = sourcePath;
        this.exportPath = exportPath;
    }

    @Override
    protected Boolean call() {

        try {
            //1 读取txt内所有图片的名称
            List<String> photoNameList = ParseDocumentTool.readFullDocument(txtFile);


            //2 到目标目录下获取所有格式为jpg的文件
            Map<String, File> photoMap = ParsePhotoTool.getAllJPGFile(sourcePath,new HashMap<>());

            //3 用txt中得到的图片名称与目录下得到的文件名称匹配，相符合的组成一个map<key(图片名称),value(图片路径)>
            Map<String, File> resultPhotoMap = new HashMap<String, File>();
            File photo = null;
            StringBuilder pNameStr= new StringBuilder();
            for(String pName: photoNameList){
                if((photo = photoMap.get(FilenameUtils.getName(pName).toLowerCase()))!=null){
                    resultPhotoMap.put(photo.getName(), photo);
                }else{
                    notFind++;
                }
            }

            updateMessage("共找到"+resultPhotoMap.size()+"张照片 \n");

            if (notFind > 0){
                updateMessage(notFind + "张照片未找到 \n");
            }

            //4 执行复制,获取输出目录，拼接路径执行移动
            updateMessage("准备开始移动照片...\n");

            Iterator<Map.Entry<String, File>> photoIterable = resultPhotoMap.entrySet().iterator();
            Map.Entry<String,File> entry;
            File newFile = null;
            Integer nowIndex = 0;
            Integer maxIndex = resultPhotoMap.size();  // 文件总数量
            while (photoIterable.hasNext()){
                entry = photoIterable.next();
                newFile = FileOperationUtil.updateFilePath(exportPath,entry, false);
                if(newFile == null){
                    throw new IOException("新文件信息为空");
                }
                if(!newFile.getParentFile().exists()){  // 假如文件路径不存在则创建
                    newFile.getParentFile().mkdirs();
                }
                if(newFile.exists()){ // 假如文件已经存在则改名称
                    newFile = FileOperationUtil.updateFilePath(exportPath,entry, true);
                }
                entry.getValue().renameTo(newFile);// 执行复制
                nowIndex++;
                updateProgress(nowIndex, maxIndex);// 刷新进度条
            }
        }catch (IOException e){
            Platform.runLater(()->{
                new Alert(Alert.AlertType.ERROR, "未知错误，意外终止");
            });
            e.printStackTrace();
        }

        return null;
    }


}
