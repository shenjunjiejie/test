package com.dfsdk.ocr.scan.network;

import android.graphics.Bitmap;

import com.dfsdk.ocr.scan.BuildConfig;
import com.dfsdk.ocr.scan.network.callback.DFNetworkCallback;
import com.dfsdk.ocr.scan.network.request.DFApiParameter;
import com.dfsdk.ocr.scan.network.request.DFApiParameterList;
import com.dfsdk.ocr.scan.network.response.DFNetworkBaseModule;
import com.dfsdk.ocr.scan.network.response.DFNetworkFailureType;
import com.dfsdk.ocr.scan.network.util.DFNetworkUtil;
import com.dfsdk.ocr.scan.network.util.Platform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class DFApiManager {
    public static final long DEFAULT_MILLISECONDS = 10_000L;

    private static final String TAG = "DFApiManager";

    private Platform mPlatform;
    private String mBaseUrl;

    public DFApiManager() {
        mBaseUrl = BuildConfig.HOST_URL;
        mPlatform = Platform.get();
    }

    public void getSyn(String control, String action, DFApiParameterList headerParameterList,
                       DFApiParameterList bodyParameterList, final DFNetworkCallback<DFNetworkBaseModule> callback) {
        sendRequest(control, action, headerParameterList, bodyParameterList, RequestMethod.HttpGet, callback);
    }

    public void postSyn(String control, String action, DFApiParameterList headerParameterList,
                        DFApiParameterList bodyParameterList, final DFNetworkCallback<DFNetworkBaseModule> callback) {
        sendRequest(control, action, headerParameterList, bodyParameterList, RequestMethod.HttpPost, callback);
    }

    private static int setConnectParameter(HttpURLConnection httpConn) {
        try {
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true);
            httpConn.setConnectTimeout(40000);
            httpConn.setReadTimeout(40000);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Cache-Control", "no-cache");
            httpConn.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);
            httpConn.connect();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
        }
        return 0;
    }

    public void sendRequest(String control, String action, DFApiParameterList headerParameterList, DFApiParameterList bodyParameterList,
                            RequestMethod requestMethod, final DFNetworkCallback<DFNetworkBaseModule> callback) {
        StringBuilder urlSb = new StringBuilder();
        urlSb.append(mBaseUrl)
                .append(control)
                .append(action);

        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) new URL(urlSb.toString()).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            httpConn = null;
        }
        if (httpConn == null) {
            connectError(callback);
            return;
        }
        if (!DFNetworkUtil.isEmpty(headerParameterList)) {
            for (DFApiParameter para : headerParameterList) {
                if (para.value != null) {
                    if (para.value instanceof String) {
                        httpConn.setRequestProperty(para.name, (String) para.value);
                    }
                }
            }
        }
        int result = setConnectParameter(httpConn);
        if (result < 0) {
            connectError(callback);
            return;
        }
        OutputStream output = null;
        try {
            output = httpConn.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            output = null;
        }
        if (output == null) {
            connectError(callback);
            return;
        }
        DataOutputStream request = new DataOutputStream(output);

        try {
            setRequestPOSTPara(request, bodyParameterList);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();
            StringBuilder httpResult = new StringBuilder();
            InputStream inputStream = null;
            int responseCode = httpConn.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpConn.getInputStream();
            } else if (responseCode > 200) {
                inputStream = httpConn.getErrorStream();
            }
            String line;
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                while ((line = reader.readLine()) != null) {
                    httpResult.append("\n").append(line);
                }
                reader.close();
                httpConn.disconnect();
                if (responseCode == 200) {
                    connectSuccess(callback, httpResult.toString());
                } else {
                    requestError(callback, responseCode, httpResult.toString());
                }
            } else {
                connectError(callback);
            }
        } catch (IOException e) {
            e.printStackTrace();
            connectError(callback);
        }
    }

    private void connectError(DFNetworkCallback<DFNetworkBaseModule> callback) {
        DFNetworkBaseModule baseModule = new DFNetworkBaseModule();
        baseModule.setReason("Connection error, please check your network.");
        sendFailResult(callback, baseModule, DFNetworkFailureType.DF_ERROR_CONNECT);
    }

    private void requestError(DFNetworkCallback<DFNetworkBaseModule> callback, int httpCode, String requestResult) {
        DFNetworkBaseModule baseModule = parseRequest(requestResult);
        sendFailResult(callback, baseModule, httpCode);
    }

    private void connectSuccess(DFNetworkCallback<DFNetworkBaseModule> callback, String requestResult) {
        DFNetworkBaseModule baseModule = parseRequest(requestResult);
        sendSuccessResult(callback, baseModule);
    }

    private DFNetworkBaseModule parseRequest(String requestResult) {
        DFNetworkBaseModule baseModule = new DFNetworkBaseModule();
        try {
            JSONObject jsonObject = new JSONObject(requestResult);
            String status = jsonObject.optString("status");
            String reason = jsonObject.optString("reason");
            String requestId = jsonObject.optString("request_id");
            String results = jsonObject.optString("results");
            baseModule.setStatus(status);
            baseModule.setReason(reason);
            baseModule.setRequestId(requestId);
            baseModule.setResults(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return baseModule;
    }

    private static final String twoHyphens = "--";
    private static final String boundary = "*****";
    private static final String crlf = "\r\n";
    private static final String charset = "UTF-8";

    private void setRequestPOSTPara(DataOutputStream request, DFApiParameterList parameterList) throws IOException {
        if (!DFNetworkUtil.isEmpty(parameterList)) {
            for (DFApiParameter para : parameterList) {
                request.writeBytes(twoHyphens + boundary + crlf);
                if (para.value != null) {
                    if (para.value instanceof String) {
                        request.writeBytes("Content-Disposition: form-data; name=\"" +
                                para.name + "\"" + crlf);
                        request.writeBytes("Content-Type: text/plain; charset=" + charset + crlf);
                        request.writeBytes(crlf);
                        request.writeBytes((String) para.value + crlf);
                    } else if (para.value instanceof Integer) {
                        request.writeBytes("Content-Disposition: form-data; name=\"" +
                                para.name + "\"" + crlf);
                        request.writeBytes("Content-Type: text/plain; charset=" + charset + crlf);
                        request.writeBytes(crlf);
                        request.writeBytes(String.valueOf(para.value) + crlf);

                    } else if (para.value instanceof Bitmap) {
                        request.writeBytes("Content-Disposition: form-data; name=\"" +
                                para.name + "\"; filename=\"" +
                                para.name + ".jpg" + "\"" + crlf);
                        request.writeBytes("Content-Type: image/jpeg" + crlf);
                        request.writeBytes(crlf);
                        Bitmap bitmap = (Bitmap) para.value;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, request);
                        request.writeBytes(crlf);
                    } else if (para.value instanceof byte[]) {
                        request.writeBytes("Content-Disposition: form-data; name=\"" +
                                para.name + "\"; filename=\"" +
                                para.name + ".jpg" + "\"" + crlf);
                        request.writeBytes("Content-Type: image/jpeg" + crlf);
                        request.writeBytes(crlf);
                        byte[] data = (byte[]) para.value;
                        request.write(data);
                    }
                }
            }
        }
    }

    public void sendFailResult(final DFNetworkCallback<DFNetworkBaseModule> callback, final DFNetworkBaseModule baseModule, final int errorCode) {
        if (callback != null) {
            mPlatform.execute(new Runnable() {
                @Override
                public void run() {
                    callback.failed(errorCode, baseModule);
                }
            });
        }
    }

    public void sendSuccessResult(final DFNetworkCallback<DFNetworkBaseModule> callback, final DFNetworkBaseModule baseModule) {
        if (callback != null) {
            mPlatform.execute(new Runnable() {
                @Override
                public void run() {
                    callback.completed(baseModule);
                }
            });
        }
    }

    public enum RequestMethod {
        HttpPost,
        HttpGet;
    }
}
