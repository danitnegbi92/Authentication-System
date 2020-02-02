package com.mycheck.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getDateAfterSelectedDays(Date date, int days){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }
}
