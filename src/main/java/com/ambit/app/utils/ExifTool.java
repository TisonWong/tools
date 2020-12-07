package com.ambit.app.utils;

import java.io.*;
import java.util.Arrays;

import com.ambit.app.entity.GpsInfoEntity;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

public class ExifTool {

    public static void changExifInfo(File sourceFile, File outputFile, GpsInfoEntity gpsInfoEntity)throws IOException, ImageReadException, ImageWriteException {
        try (
                FileOutputStream fos = new FileOutputStream(outputFile);
                OutputStream os = new BufferedOutputStream(fos)
        ) {
            TiffOutputSet outputSet = null;

            ImageMetadata metadata = Imaging.getMetadata(sourceFile);
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
            }
            if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }
            {
                // 修改GPS信息
                outputSet.setGPSInDegrees(gpsInfoEntity.getLongitude(), gpsInfoEntity.getLatitude());

                TiffOutputDirectory t = outputSet.getOrCreateGPSDirectory();
                // 海拔高度 - 高度对应 <Center> 标签 <z>
                t.removeField(GpsTagConstants.GPS_TAG_GPS_ALTITUDE);    // 删除原信息
                RationalNumber rationalNumber = new RationalNumber(gpsInfoEntity.getAltitude(), 1);
                t.add(GpsTagConstants.GPS_TAG_GPS_ALTITUDE, rationalNumber);    // 载入新信息
            }
            new ExifRewriter().updateExifMetadataLossless(sourceFile, os, outputSet);   // 执行导出
        }
    }

    /**
     * 读取照片的GPS信息
     * @param sourceFile
     * @throws IOException
     * @throws ImageReadException
     */
    public static void getExifInfo(File sourceFile)throws IOException, ImageReadException{
        ImageMetadata metadata = Imaging.getMetadata(sourceFile);
        JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        if (null != jpegMetadata) {
            final TiffImageMetadata exif = jpegMetadata.getExif();
            if (null != exif) {
                System.out.println(Arrays.toString(exif.getFieldValue(GpsTagConstants.GPS_TAG_GPS_LONGITUDE)));
            }
        }
    }

}
