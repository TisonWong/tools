package com.ambit.app.controller;

import com.ambit.app.utils.FileOperationUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 筛选照片
 * 从指定文件夹中根据奇/偶数拷贝出一半的照片到指定的目录下
 */
public class ScreeningOfPhoto {

    public static void main(String[] args){


        String sourceFiles1 = "\\\\am200\\J\\20200506_TKL_trial flight\\200506 - Ping Che\\1.1";
        String sourceFiles2 = "\\\\am200\\J\\20200506_TKL_trial flight\\200506 - Ping Che\\3.1";


        File sourceFile = new File(sourceFiles1);
        File[] files = sourceFile.listFiles();


        File newFile;
        String newFolder = "\\\\am200\\" + FilenameUtils.getPath(sourceFiles1) + "1.3\\";
        for (int i = 0; i<files.length; i++){
            if (i%3==0){
                newFile = new File(newFolder + files[i].getName());
                if (!newFile.exists()){
                    newFile.getParentFile().mkdirs();
                }
                int finalI = i;
                File finalNewFile = newFile;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileOperationUtil.copyFileUsingFileStreams(files[finalI], finalNewFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                executorService.submit(thread);
            }
        }
        executorService.shutdown();
    }

    private static ExecutorService executorService = Executors.newFixedThreadPool(10,getThreadFactory());

    private static ThreadFactory getThreadFactory(){
        return new ThreadFactory() {
            AtomicInteger sn = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                SecurityManager s = System.getSecurityManager();
                ThreadGroup group = (s!=null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                Thread t = new Thread(group, r);
                System.err.println("任务线程: "+sn.incrementAndGet());
                return t;
            }
        };
    }

}
