package com.ambit.app.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换
 */
public class PowerONv2 {

    public static void main(String[] args) throws Exception {

        String dateStr = "2019-12-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date =sdf.parse(dateStr);
        System.out.println(date.getTime());
    }
}