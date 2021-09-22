package wappsto.iot.rpc;

import java.nio.charset.*;
import java.util.*;

public class Utils {
    private static String identifier = "";
    private static int getCount = 0;
    private static int putCount = 0;
    private static int postCount = 0;

    public static String requestId(Methods method) {
        if (identifier.isEmpty()) {
            byte[] array = new byte[7];
            new Random().nextBytes(array);
            identifier = new String(array, Charset.forName("UTF-8")) + "_";
        }
        int count = 0;
        switch (method) {
            case GET:
                getCount++;
                count = getCount;
                break;
            case PUT:
                putCount++;
                count = putCount;
                break;
            case POST:
                postCount++;
                count = postCount;
                break;
        }
        return identifier + method + count;
    }
}
