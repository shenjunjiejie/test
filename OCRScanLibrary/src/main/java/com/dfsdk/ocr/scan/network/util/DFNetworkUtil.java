package com.dfsdk.ocr.scan.network.util;


import com.dfsdk.ocr.scan.util.DFOCRScanUtils;

import java.util.List;

public class DFNetworkUtil {
    public static void logI(Object... logValue) {
        DFOCRScanUtils.logI(logValue);
    }

    public static <T> boolean isEmpty(List<T> list) {
        boolean isEmpty = true;
        if (list != null && !list.isEmpty()) {
            isEmpty = false;
        }
        return isEmpty;
    }
}
