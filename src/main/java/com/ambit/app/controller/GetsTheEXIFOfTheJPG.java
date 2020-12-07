package  com.ambit.app.controller;

import com.ambit.app.utils.JavaFXFileSelectTool;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetsTheEXIFOfTheJPG implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField folderText;

    @FXML
    private Button folderBtn;

    @FXML
    private TreeView<MyEXIF> exifTreeView;

    private TreeItem<MyEXIF> exifRoot = new TreeItem<>();

    TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");

    @FXML
    void selectPath() throws ImageProcessingException, IOException {
        DirectoryChooser directoryChooser = JavaFXFileSelectTool.showDirectoryChooser();
        File photoPath = directoryChooser.showDialog(root.getScene().getWindow());
        if (null == photoPath){return;}
        JavaFXFileSelectTool.setLastUsePath(photoPath.getPath());
        folderText.setText(photoPath.getPath());
        getExifByPath();
    }

    void getExifByPath() throws ImageProcessingException, IOException {
        exifRoot.getChildren().clear();
        // 解析工具
        Metadata metadata;
        ExifSubIFDDirectory exifSubIFDDirectory;
        // timeZone GMT+8 表示中国东八区
//		TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // 读取目录下的所有文件
        File[] photoFiles = new File(folderText.getText()).listFiles();
        String extension;
        MyEXIF myEXIF;
        for(File photo : photoFiles){
            // 传入文件名称或路径, 返回该文件的扩展名称, 如 "exe","jpg","cr2" ...
            extension = FilenameUtils.getExtension(photo.getPath());
            if (!"cr2".equals(extension.toLowerCase()) && !"jpg".equals(extension.toLowerCase())){
                // 过滤非.cr2或.jpg的文件
                continue;
            }
            metadata = ImageMetadataReader.readMetadata(photo);
            exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            // 当@exifSubIFDDirectory为空, 表示当前照片文件缺少必要数据,无法继续解析, 否则会报空指针异常
            if (null == exifSubIFDDirectory){
                // 照片缺少exif数据
                continue;
            }
            myEXIF = new MyEXIF();
            myEXIF.setName(photo.getName());
            // getDate(日期类型:拍摄时间或其它,日期的次秒级值,时区); 此处获取到照片的拍摄时间, 格式是: yyyy-MM-dd HH:mm:ss.SSS
            myEXIF.setShootingDate(exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL,exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_SUBSECOND_TIME_DIGITIZED),timeZone));
            exifRoot.getChildren().add(new TreeItem<>(myEXIF));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exifTreeView.setRoot(exifRoot);
        exifTreeView.setShowRoot(false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        exifTreeView.setCellFactory(param -> new TextFieldTreeCell<>(new StringConverter<MyEXIF>() {
            @Override
            public String toString(MyEXIF node) {
                if (null == node) return null;
                return node.getName()+"\t"+sdf.format(node.getShootingDate());
            }

            @Override
            public MyEXIF fromString(String string) {
                return null;
            }
        }));
    }
}

class MyEXIF{
    private String name;
    private Date shootingDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getShootingDate() {
        return shootingDate;
    }

    public void setShootingDate(Date shootingDate) {
        this.shootingDate = shootingDate;
    }
}
