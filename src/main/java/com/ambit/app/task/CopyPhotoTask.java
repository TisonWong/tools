package com.ambit.app.task;

import com.ambit.app.utils.FileOperationUtil;
import com.ambit.app.utils.ParseDocumentTool;
import com.ambit.app.utils.ParsePhotoTool;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 执行照片复制任务
 */
public class CopyPhotoTask extends Task<Boolean> {

    private String txtFile, sourcePath, exportPath;
    private Integer notFind = 0;

    public CopyPhotoTask(String txtFile, String sourcePath, String exportPath) {
        this.txtFile = txtFile;
        this.sourcePath = sourcePath;
        this.exportPath = exportPath;
    }

    @Override
    protected Boolean call() throws Exception {
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
            pNameStr.setLength(0);
        }
        updateMessage("共找到"+resultPhotoMap.size()+"张照片 \n");

        if (notFind > 0){
            updateMessage(notFind + "张照片未找到 \n");
        }

        updateMessage("准备开始复制照片...\n");

        Iterator<Map.Entry<String, File>> photoIterable = resultPhotoMap.entrySet().iterator();
        Map.Entry<String,File> entry;
        File newFile = null;
        File createFolder = null;
        int nowIndex = 0;
        int maxIndex = resultPhotoMap.size();
        FileOperationUtil fileOperationUtil = new FileOperationUtil();
        while (photoIterable.hasNext()){
            nowIndex++;
            entry = photoIterable.next();
            newFile = FileOperationUtil.updateFilePath(exportPath, entry, false);
            // 假如文件路径不存在则创建
            if(!newFile.getParentFile().exists()){
                newFile.getParentFile().mkdirs();
            }
            // 假如文件已经存在则改名称
            if(newFile.exists()){
                newFile = FileOperationUtil.updateFilePath(exportPath, entry, true);
            }
            // 执行复制
            fileOperationUtil.copyFileUsingFileChannels(entry.getValue(),newFile);
            // 刷新进度条
            updateProgress(nowIndex, maxIndex);
        }
        updateMessage("所有照片移动完成\n");
        return true;
    }







}
