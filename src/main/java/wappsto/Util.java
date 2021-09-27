package wappsto;

import java.text.*;
import java.util.*;

public class Util {
    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"
        );
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
}
