package temp;

import com.ambit.app.utils.ParseDocumentTool;
import com.ambit.app.utils.WriteDataToFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 读取LIDAR Json文档数据
 */
public class LIDARData {

    public static void main(String[] args) {
        // 原始文件的路径
        String flightFilePath = "L:\\20201023_GEO_LYM_LiDAR_MTLS\\Project\\LYM Cylone M+\\f4.json";
        // 文件导出路径
        String outputPath = "D:\\@Ben\\Test\\ChangeXYZ";


        File flightFile = new File(flightFilePath);
        List<Point> pointList = readFlightFile(flightFilePath);

        // 排序, reversed:倒序
        pointList.sort(Comparator.comparingInt(Point::getReturnNum).reversed());

        for (int i=0;i<10;i++) {
            System.out.println(pointList.get(i).getReturnNum());
        }
//        StringBuffer stringBuffer = new StringBuffer();
//        for (Point point : pointList) {
//            stringBuffer.append(point.toString());
//        }
//        WriteDataToFile.resultWrite(stringBuffer.toString(),outputPath, FilenameUtils.getBaseName(flightFile.getName())+".txt",true);
    }

    /**
     * 读取指定Flight文档
     * @param flightFile
     * @return
     */
    public static List<Point> readFlightFile(String flightFile){
        ArrayList<String> arrayList = ParseDocumentTool.readFullDocument(flightFile);
        Point point;
        List<Point> pointList = new ArrayList<>();
        for (String s : arrayList) {
            point = new Point();
            String[] split = s.split("\\t");
            point.setX((Double.parseDouble(split[0])*-1));
            point.setY((Double.parseDouble(split[1])*-1));
            point.setZ((Double.parseDouble(split[2])*-1));
            point.setReturnNum(Integer.parseInt(split[3]));
            pointList.add(point);
        }
        return pointList;
    }
}


class Point{
    private Double x;
    private Double y;
    private Double z;
    private Integer returnNum;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    @Override
    public String toString() {
        return z+"\t"+y+"\t"+x+"\t"+ returnNum +"\n";
    }
}