package com.kenneth.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyWebsocketUtil {

    public static List<Map<String, String>> CONNECTION_KEYS= new ArrayList();

    public static List<Map<String, String>> getConnectionKeys() {
        return CONNECTION_KEYS;
    }

    public static void setConnectionKeys(List<Map<String, String>> connectionKeys) {
        CONNECTION_KEYS = connectionKeys;
    }
}
