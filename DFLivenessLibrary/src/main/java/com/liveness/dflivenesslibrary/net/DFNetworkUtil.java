package com.liveness.dflivenesslibrary.net;

import android.util.Log;

import com.liveness.dflivenesslibrary.BuildConfig;
import com.liveness.dflivenesslibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class DFNetworkUtil {
    private final static String TAG = DFNetworkUtil.class.getSimpleName();
    private static final String HOST_URL = BuildConfig.HOST_URL;
    private static final String SILENT_ANTI_HACK_URL = HOST_URL + "/face/liveness_anti_hack";
    // The hack score threshold value is 0.98 which is get by training from big industry data,
    // he/she is real person if score smaller than the threshold.
    private static final double ANTI_HACK_THRESHOLD = 0.98f;
    // Contact to Accuauth to get the correct value of API_ID& API_SECRET
    public static final String API_ID = "305420bbe8f54b90b4ebc840d81feae4";
    public static final String API_SECRET = "0f95b3a100dc4976bb6ead969e3b19cb";

    public static DFNetResult doAntiHack(byte[] data) {
        HashMap<String, byte[]> hashMap = new HashMap<>(1);
        hashMap.put("liveness_data_file", data);
        HashMap<String, String> hashParams = new HashMap<>(1);
        DFNetResult dfNetResult = new DFNetResultLiveness();
        return doInternal(SILENT_ANTI_HACK_URL, hashParams, hashMap, dfNetResult, new NetworkResultProcess() {
            @Override
            public void resultProcess(DFNetResult result, JSONObject netResult) {
                double score = netResult.optDouble("score");

                result.mNetworkResultStatus = score < ANTI_HACK_THRESHOLD;
                if (!result.mNetworkResultStatus) {
                    result.mNetworkErrorMsgID = R.string.livenesslibrary_detect_failed;
                }
            }
        });
    }

    private static DFNetResult doInternal(String url, HashMap<String, String> hashParams, HashMap<String, byte[]> hashMap, DFNetResult dfNetResult, NetworkResultProcess resultProcess) {
        String result = DFHttpManager.doPost(url, hashParams, hashMap);
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.optString("status");
                if (!status.isEmpty() && status.equalsIgnoreCase("OK")) {
                    if (resultProcess != null) {
                        resultProcess.resultProcess(dfNetResult, jsonObject);
                    }
//                    dfNetResult.mNetworkErrorMsgID = -1;
                } else {
                    dfNetResult.mNetworkResultStatus = false;
                    dfNetResult.setErrorMsgID(status);
                }
            } else {
                dfNetResult.mNetworkResultStatus = false;
                dfNetResult.mNetworkErrorMsgID = R.string.string_network_error_connect_host_error;
            }

        } catch (JSONException e) {
            Log.e(TAG, "json parse failed");
            e.printStackTrace();
        }

        return dfNetResult;
    }

    private interface NetworkResultProcess {
        void resultProcess(DFNetResult result, JSONObject netResult);
    }


    public static class DFNetResult {
        protected HashMap<String, Integer> mHashMap = new HashMap<>();

        protected void addSpecialMsg() {
        }

        public DFNetResult() {
            mHashMap.put("INVALID_ARGUMENT", R.string.string_network_error_invalid_argument);
            mHashMap.put("DOWNLOAD_ERROR", R.string.string_network_error_download_error);
            mHashMap.put("UNAUTHORIZED", R.string.string_network_error_unauthorized);
            mHashMap.put("RATE_LIMIT_EXCEEDED", R.string.string_network_error_rate_limit_exceeded);
            mHashMap.put("NOT_FOUND", R.string.string_network_error_not_found);
            mHashMap.put("INTERNAL_ERROR", R.string.string_network_error_internal_error);
            mHashMap.put("RPC_TIMEOUT", R.string.string_network_error_rpc_timeout);
            mHashMap.put("CONNECTION_TIMEOUT", R.string.string_network_error_timeout);
            mHashMap.put("CONNECTION_ERROR", R.string.string_network_error_connect_host_error);
            addSpecialMsg();
        }

        public boolean mNetworkResultStatus = false;
        public int mNetworkErrorMsgID = -1;

        public void setErrorMsgID(String status) {
            mNetworkErrorMsgID = getErrorMsgIDByStatus(status);
        }

        private int getErrorMsgIDByStatus(String status) {
            if (mHashMap.containsKey(status)) {
                return mHashMap.get(status);
            }

            return -1;
        }
    }

    public static class DFNetResultLiveness extends DFNetResult {
        @Override
        protected void addSpecialMsg() {
            mHashMap.put("DETETION_FAILED", R.string.string_network_error_detection_failed);
            mHashMap.put("WRONG_LIVENESS_DATA", R.string.string_network_error_wrong_liveness_data);
            mHashMap.put("NO_FACE_DETECTED", R.string.string_network_error_detection_failed);
        }
    }

}
