package com.dfsdk.ocr.scan.network.model;

import java.io.Serializable;

public class DFCardInfo extends DFCardBaseInfo implements Serializable {

    public static final String CARD_TYPE_AADHAAR_CARD_FRONT = "aadhaar_card_front";
    public static final String CARD_TYPE_AADHAAR_CARD_BACK = "aadhaar_card_back";
    public static final String CARD_TYPE_PAN_CARD = "pan_card";

    private String cardType;
    private byte[] detectImage;
    private Object cardInfo;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public byte[] getDetectImage() {
        return detectImage;
    }

    public void setDetectImage(byte[] detectImage) {
        this.detectImage = detectImage;
    }

    public Object getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(Object cardInfo) {
        this.cardInfo = cardInfo;
    }
}
