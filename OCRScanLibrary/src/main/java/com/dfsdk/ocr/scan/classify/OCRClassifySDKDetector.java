package com.dfsdk.ocr.scan.classify;

import android.content.Context;
import android.graphics.Rect;

import com.dfsdk.ocr.classify.sdk.DFOCRClassifyResult;
import com.dfsdk.ocr.classify.sdk.DFOCRClassifySDK;
import com.dfsdk.ocr.scan.detect.DetectInterface;
import com.dfsdk.ocr.scan.util.DFOCRScanUtils;

import java.util.ArrayList;
import java.util.List;

public class OCRClassifySDKDetector implements DetectInterface<DFOCRClassifyResult> {
    private static final Object TAG = "OCRClassifySDKDetector";

    private DFOCRClassifySDK mOCRClassifySDK;

    private int mQualifiedCount;
    private float mQualifiedScore;
    private List<DFOCRClassifyResult> mOCRClassifyResultList;

    public OCRClassifySDKDetector(int qualifiedCount, float qualifiedScore) {
        mQualifiedCount = qualifiedCount;
        mQualifiedScore = qualifiedScore;
        mOCRClassifyResultList = new ArrayList<>();
    }

    @Override
    public int init(Context context) {
        if (mOCRClassifySDK == null) {
            mOCRClassifySDK = new DFOCRClassifySDK(context);
        }
        return mOCRClassifySDK.init(context);
    }

    @Override
    public DFOCRClassifyResult detect(byte[] data, int width, int height, Rect scanRect, int degree) {
        DFOCRClassifyResult ocrClassifyResult = null;
        if (mOCRClassifySDK != null) {
            long detectStartTime = System.currentTimeMillis();
            ocrClassifyResult = mOCRClassifySDK.detect(data, width, height, scanRect, degree);
            long detectSpaceTime = System.currentTimeMillis() - detectStartTime;
            DFOCRScanUtils.logI(TAG, "time_statistical", "ocr classify", "detect time(s)", detectSpaceTime / 1000.0f);
            DFOCRScanUtils.logI(TAG, "ocrClassify", ocrClassifyResult.getClassifyResult(), ocrClassifyResult.getClassifyScore());
            if (ocrClassifyResult.getClassifyResult() == DFOCRClassifySDK.IMAGE_QUALIFIED) {
                mOCRClassifyResultList.add(ocrClassifyResult);
            } else {
                DFOCRScanUtils.logI(TAG, "ocrClassify", "no match", ocrClassifyResult.getClassifyResult(), ocrClassifyResult.getClassifyScore());
                mOCRClassifyResultList.clear();
            }
        }
        return ocrClassifyResult;
    }

    @Override
    public DFOCRClassifyResult isFinish(DFOCRClassifyResult ocrClassifyResult) {
        DFOCRClassifyResult qualifiedResult = null;
        int classifySize = mOCRClassifyResultList.size();
        if (classifySize >= mQualifiedCount) {
            int qualifiedIndex = classifySize - 1;
            qualifiedResult = mOCRClassifyResultList.get(qualifiedIndex);
            DFOCRScanUtils.logI(TAG, "ocrClassify", qualifiedIndex, "qualifiedResult", qualifiedResult.getClassifyResult(), qualifiedResult.getClassifyScore());
            mOCRClassifyResultList.clear();
        }
        return qualifiedResult;
    }

    @Override
    public void release() {
        if (mOCRClassifySDK != null) {
            mOCRClassifySDK.release();
            mOCRClassifySDK = null;
        }
    }
}
