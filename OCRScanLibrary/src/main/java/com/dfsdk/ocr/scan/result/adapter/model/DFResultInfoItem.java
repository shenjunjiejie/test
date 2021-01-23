package com.dfsdk.ocr.scan.result.adapter.model;

import android.graphics.Bitmap;

/**
 * Copyright (c) 2018-2019 DEEPFINCH Corporation. All rights reserved.
 */

public class DFResultInfoItem {
    private DFResultInfoItemType infoItemType;
    private Bitmap image;
    private String title;
    private boolean checked;
    private String content;
    private Bitmap rightImage;
    private boolean rightChecked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public DFResultInfoItemType getInfoItemType() {
        return infoItemType;
    }

    public void setInfoItemType(DFResultInfoItemType infoItemType) {
        this.infoItemType = infoItemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getRightImage() {
        return rightImage;
    }

    public void setRightImage(Bitmap rightImage) {
        this.rightImage = rightImage;
    }

    public boolean isRightChecked() {
        return rightChecked;
    }

    public void setRightChecked(boolean rightChecked) {
        this.rightChecked = rightChecked;
    }

    public enum DFResultInfoItemType {
        TYPE_CARD_IMAGE_SINGLE(1),
        TYPE_CARD_IMAGE_DOUBLE(2),
        TYPE_USER_INFO(3),
        TYPE_TRY_AGAIN(4),
        TYPE_EXTRACT_SUCCESS(5),
        TYPE_EXTRACT_FAIL(6);
        private int type;

        DFResultInfoItemType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
