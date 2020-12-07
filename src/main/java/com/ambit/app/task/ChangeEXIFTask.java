package com.ambit.app.task;

import com.ambit.app.entity.GpsInfoEntity;
import com.ambit.app.process.ExecutorProcessPool;
import com.ambit.app.process.ExecutorServiceFactory;
import com.ambit.app.utils.ExifTool;
import com.ambit.app.utils.FileOperationUtil;
import com.ambit.app.utils.ParseDocumentTool;
import com.ambit.app.utils.ParsePhotoTool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChangeEXIFTask extends Task<Boolean> {

    private String xmlPath, photoPath, exportText;
    // 是否开启多线程
    private Boolean startMultithreading;
    private Integer threadCount;

    // 创建线程池
    ExecutorServiceFactory serviceFactory = ExecutorServiceFactory.getInstance();
    ExecutorService pool = null;

    public ChangeEXIFTask(String xmlPath, String photoPath, String exportText) {
        this.xmlPath = xmlPath;
        this.photoPath = photoPath;
        this.exportText = exportText;
        startMultithreading = false;
    }
    public ChangeEXIFTask(String xmlPath, String photoPath, String exportText,Boolean startMultithreading, Integer threadCount) {
        this.xmlPath = xmlPath;
        this.photoPath = photoPath;
        this.exportText = exportText;
        this.startMultithreading = startMultithreading;
        this.threadCount = threadCount;
        pool = serviceFactory.createFixedThreadPool(threadCount);
    }

    @Override
    protected Boolean call(){

        FileOperationUtil fileOperationUtil = new FileOperationUtil();

        // 记录未找到的照片数量
        int notFindCount = 0;
        try {
            updateMessage("开始解析XML文档");
            // 1: 解析XML文档
            // parseAtXMLMap <照片路径以及名称, GPS数据实体类>
            Map<String, GpsInfoEntity> parseAtXMLMap = null;

            if (FilenameUtils.getExtension(xmlPath).toLowerCase().equals("xml")){
                parseAtXMLMap = ParseDocumentTool.parseAtXML(new File(xmlPath));
            }else if (FilenameUtils.getExtension(xmlPath).toLowerCase().equals("txt")){
                parseAtXMLMap = ParseDocumentTool.parsePosTXT(new File(xmlPath));
            }

            if (parseAtXMLMap.size() <1){
                updateMessage("XML文档未解析到数据, 执行结束");
                return false;
            }
            // 2,执行GPS信息修改


            Queue<HandlePhotoTask> taskQueue = new LinkedList<>();

            File sourceFile = null;
            File newFile = null;
            int nowIndex = 0;
            int totalIndex = parseAtXMLMap.size();
            StringBuilder stringBuilder = new StringBuilder();
            for(Map.Entry<String,GpsInfoEntity> gpsEntry: parseAtXMLMap.entrySet()){
                sourceFile = new File(gpsEntry.getKey());

                // 如果文件不存在，就执行查找
                if(!sourceFile.exists()){
                    sourceFile = byNameFindFile(photoPath, gpsEntry.getKey());
                    if(null == sourceFile){
                        notFindCount++;
                        // 刷新进度条
//                        nowIndex++;
                        updateProgress((nowIndex+notFindCount), totalIndex);
                        continue;
                    }
                }
                // 拼接导出新文件名称及路径: 导出路径/原照片的父级目录/照片名称
                stringBuilder.setLength(0);
                stringBuilder.append(exportText).append(File.separator)
                        .append(sourceFile.getParentFile().getName()).append(File.separator)
                        .append(sourceFile.getName());
                newFile = new File(stringBuilder.toString());
                updateMessage("正在处理: "+sourceFile.getName() + " 丨实际处理数量: ["+nowIndex+"]丨处理进度: [ " +(nowIndex+notFindCount) +"/"+ totalIndex+" ]");

                // 是否使用多线程执行
                // 在这里创建线程池并监听关闭
                if(startMultithreading){
                    HandlePhotoTask handlePhotoTask = new HandlePhotoTask(newFile, sourceFile, gpsEntry.getValue());
                    taskQueue.offer(handlePhotoTask);
                    // 刷新进度条
                    nowIndex++;
                    if (taskQueue.size() >= threadCount || (totalIndex - nowIndex) <= threadCount){
                        for (HandlePhotoTask task: taskQueue){
                            pool.execute(task);
                        }
                        // 清空队列
                        taskQueue.clear();
                        while (true){
                            if (pool.isTerminated()){
                                updateProgress(nowIndex, totalIndex);
                                break;
                            }
                        }
                    }
                }else{
                    if(!newFile.exists()){
                        // 复制图片
                        fileOperationUtil.copyFileUsingFileChannels(sourceFile,newFile);
                    }
                    ExifTool.changExifInfo(sourceFile, newFile, gpsEntry.getValue());
                    // 刷新进度条
                    nowIndex++;
                    updateProgress(nowIndex, totalIndex);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"未知错误,执行终止\n"+e.getMessage()).show();
        }finally {
            // 清空缓存
            photoMap.clear();
            if (null!=pool){
                pool.shutdown();
            }
        }
        updateMessage("全部处理完毕, 其中["+notFindCount+"]条记录找不到照片");
        return true;
    }

    // 保存所有照片映射对象
    private Map<String,File> photoMap = new HashMap<>();
    /**
     * 首次执行时检索目录下所有照片文件保存到Map
     * 后续直接从Map中查找照片映射对象
     * @param photoPath
     * @param fileName
     * @return
     */
    public File byNameFindFile(String photoPath, String fileName){
        if(photoMap.size()<1){
            updateMessage("正在检索目录 : "+photoPath+" 所有照片...");
            ParsePhotoTool.getAllJPGFile(photoPath, photoMap);
            updateMessage("检索到照片数量: "+photoMap.size());
        }
        return photoMap.get(FilenameUtils.getBaseName(fileName).toLowerCase());
    }


    /**
     * 多线程文件处理任务
     */
    static class HandlePhotoTask extends Task<Boolean>{
        private File newFile, sourceFile;
        private GpsInfoEntity gpsInfoEntity;

        public HandlePhotoTask(File newFile, File sourceFile, GpsInfoEntity gpsInfoEntity) {
            this.newFile = newFile;
            this.sourceFile = sourceFile;
            this.gpsInfoEntity = gpsInfoEntity;
        }

        @Override
        protected Boolean call() throws Exception {
            FileOperationUtil fileOperationUtil = new FileOperationUtil();
            if(!newFile.exists()){
                // 复制图片
                fileOperationUtil.copyFileUsingFileChannels(sourceFile,newFile);
            }
            ExifTool.changExifInfo(sourceFile, newFile, gpsInfoEntity);
            return true;
        }
    }

}
