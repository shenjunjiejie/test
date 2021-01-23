package com.dfsdk.ocr.scan.config;

public class DFOCRScanConfig {
    private static final class InstanceHolder {
        private static final DFOCRScanConfig INSTANCE = new DFOCRScanConfig();
    }

    public static DFOCRScanConfig getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static DFOCRScanConfig getCleanInstance() {
        DFOCRScanConfig config = getInstance();
        config.initDefaultValue();
        return config;
    }

    private void initDefaultValue() {

    }
}
