package temp;

import com.ambit.app.utils.FileOperationUtil;
import com.ambit.app.utils.ParseDocumentTool;
import com.ambit.app.utils.ParsePhotoTool;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckImages {

    public static void main(String[] args) throws IOException {

        averageDistribution();
//        checkImages();

    }

    /**
     * 平均分配模型文件到多个硬盘
     */
    public static void averageDistribution() throws IOException {
        // 定义目标磁盘
        File targetDisk = new File("H:\\");
        // 定义分块数量
        int partNum = 5;
        // 定义文件夹名称前缀
        String fileName = "part_";


        File targetFile = new File("H:\\Models");
        List<File> targetList = new ArrayList<>();
        ParseDocumentTool.getAllFiles(targetFile,targetList,"3dml");
        System.err.println(targetList.size());

        int i = 0;
        File newFile;
        // 遍历文件集合
        for (File file : targetList) {
            i++;
            newFile = new File(targetDisk+fileName+(i+1),file.getName());

            if (!newFile.getParentFile().exists()) newFile.getParentFile().mkdirs();

            file.renameTo(newFile);  // 移动
//            FileOperationUtil.copyFileUsingFileStreams(file,new File(targetDisk+fileName+(i+1),file.getName()));  //复制
            if (i==partNum){
                i=0;
            }
        }

    }

    /**
     * 比对两个文件夹之间照片是否相等
     */
    public static void checkImages(){
        // 读取参照文件夹内所有照片
        // 保存到Map集合-sourceMap
        File sourceFile = new File("H:\\");
        Map<String,File> sourceMap = new HashMap<>();
        ParseDocumentTool.getAllFiles(sourceFile,sourceMap,"3dml");
        System.err.println(sourceMap.size());
        // 读取被比较文件夹内所有照片
        // 保存到List集合-targetList
        File targetFile = new File("D:\\3D_Models\\3DML\\HK_Island-51Blocks");
        List<File> targetList = new ArrayList<>();
        ParseDocumentTool.getAllFiles(targetFile,targetList,"3dml");
        System.err.println(targetList.size());

        // 遍历targetList获取图片名称，调用sourceMap的get方法
        for (File file : targetList) {
            // 假如获取不为空则删除sourceMap中相应键值对
            if (sourceMap.containsKey(FilenameUtils.getBaseName(file.getName()))){
                sourceMap.remove(FilenameUtils.getBaseName(file.getName()));
                continue;
            }
            // 假如获取为空则输出targetList中当前元素的文件名称
            System.err.println(file.getName());
        }
        System.out.println("=============================");
        // 遍历结束后打印sourceMap剩余内容
        for (Map.Entry<String,File> entry:sourceMap.entrySet()){
            System.out.println(entry.getKey());
        }
    }

}


