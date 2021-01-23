package com.lirongyuns.happyrupees.bean;

import android.graphics.Bitmap;

public class LivenessInfo {

    private Bitmap idFace;
    private Bitmap panFace;
    private Bitmap liveness;

    public Bitmap getIdFace() {
        return idFace;
    }

    public void setIdFace(Bitmap idFace) {
        this.idFace = idFace;
    }
    
    public Bitmap getPanFace() {
        return panFace;
    }

    public void setPanFace(Bitmap panFace) {
        this.panFace = panFace;
    }
    

    public Bitmap getLiveness() {
        return liveness;
    }

    public void setLiveness(Bitmap liveness) {
        this.liveness = liveness;
    }

    //recycle the bitmap
    public void release() {
        if (null != idFace) {
            idFace.recycle();
        }
        if (null != panFace) {
            panFace.recycle();
        }
    
        if (null != liveness) {
            liveness.recycle();
        }
        idFace = null;
        panFace = null;
        liveness = null;
    }
}
