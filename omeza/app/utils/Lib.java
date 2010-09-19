package utils;

import java.util.Date;

public class Lib {

    static final long ONE_HOUR = 60 * 60 * 1000L;

    public static long daysDifference(Date d1, Date d2) {
        return ( (d2.getTime() - d1.getTime() + ONE_HOUR) / 
                (ONE_HOUR * 24));
    }
}
