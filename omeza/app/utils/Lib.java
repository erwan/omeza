package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Lib {

    static final long ONE_HOUR = 60 * 60 * 1000L;

    public static long daysDifference(Date d1, Date d2) {
        return ( (d2.getTime() - d1.getTime() + ONE_HOUR) / (ONE_HOUR * 24));
    }

    // Java is retarded
    public static int getYear(Date date) {
        Calendar gcalendar = new GregorianCalendar(); 
        gcalendar.setTime(date); 
        return gcalendar.get(Calendar.YEAR);
    }

}
