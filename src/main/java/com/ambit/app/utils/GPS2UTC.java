package com.ambit.app.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * GPS时间转换UTC时间
 */
public class GPS2UTC {

    /*
    GPSSecond   Year    Month   Day Hour    Minute  Second  Millisecond
    460282.672  2016    12  16  07  51  22  671 543049.080  3366449.030
    460282.677  2016    12  16  07  51  22  676 543049.053  3366449.049
     */
    public void gps2utc(){
        String gpsTime = "2016-12-16 07:51:22.671";
        Calendar calendar;
    }

    public void gps2utc2(){
        int week=1283; //gps week
        int sec=229015; //gps seconds


        GregorianCalendar gc1 = new GregorianCalendar(1980,Calendar.JANUARY,6,0,0,0);
        GregorianCalendar c = new GregorianCalendar();

        long elaps = gc1.getTimeInMillis();
        System.out.println("Elapsed seconds: " + elaps);

        long time = elaps + week*7*24*3600*1000 + sec*1000;

        System.out.println("Time  "+ time);

        Date javaDate = new Date(time);
        c.setTime(javaDate);

        System.out.print(c.get(Calendar.MONTH));
        System.out.print("-"+c.get(Calendar.DATE));
        System.out.print("-" + c.get(Calendar.YEAR));
        System.out.print(" "+ c.get(Calendar.HOUR));
        System.out.print(":" + c.get(Calendar.MINUTE));
        System.out.print(":" + c.get(Calendar.SECOND));
    }

    public static void main(String[] args) {
//        GPS2UTC gps2UTC = new GPS2UTC();
//        gps2UTC.gps2utc2();
    }

}
