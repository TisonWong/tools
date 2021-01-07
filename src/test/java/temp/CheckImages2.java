package temp;

import com.ambit.app.utils.ParseDocumentTool;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckImages2 {

    public static void main(String[] args) throws IOException {

//        averageDistribution();
        checkImages();

    }
    /**
     * 比对两个文件夹之间照片是否相等
     * 相对缺失的文件名称
     */
    public static void checkImages(){
        // 读取参照文件夹内所有照片
        // 保存到Map集合-sourceMap
        File sourceFile = new File("G:\\1211");
        Map<String,File> sourceMap = new HashMap<>();
        ParseDocumentTool.getAllFiles(sourceFile,sourceMap,"raf");
        System.err.println("source images number："+sourceMap.size());
        // 读取被比较文件夹内所有照片
        // 保存到List集合-targetList
        File targetFile = new File("G:\\1211\\RAW_JPG");
        List<File> targetList = new ArrayList<>();
//        ParseDocumentTool.getAllFiles(targetFile,targetList,"cr2");
        ParseDocumentTool.getAllFilesByFormat(targetFile,targetList,"jpg");
        System.err.println("target images number："+targetList.size());

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
