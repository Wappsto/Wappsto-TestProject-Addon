package wappsto.iot.rpc;

import org.apache.commons.lang3.*;

import java.nio.charset.*;
import java.util.*;

public class Utils {
    private static String identifier = "";
    private static int getCount = 0;
    private static int putCount = 0;
    private static int postCount = 0;

    public static String requestId(Methods method) {
        if (identifier.isEmpty()) {
            identifier = RandomStringUtils.randomAlphanumeric(10) + "_";
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
