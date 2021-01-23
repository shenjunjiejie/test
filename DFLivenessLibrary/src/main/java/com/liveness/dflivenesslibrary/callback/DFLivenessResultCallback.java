package com.liveness.dflivenesslibrary.callback;


import com.dfsdk.liveness.DFLivenessSDK;

public interface DFLivenessResultCallback {
    void saveFinalEncrytFile(byte[] livenessEncryptResult, DFLivenessSDK.DFLivenessImageResult[] imageResult);

    void saveFile(byte[] livenessEncryptResult);

    void deleteLivenessFiles();
}
