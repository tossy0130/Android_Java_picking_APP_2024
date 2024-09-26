package com.example.oracleconnect;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static String getNowDate_Sagyou_Start() {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm::ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }



}

