package com.example.pety.objects;

public class itemData {
    private int mImage;
    private String mLine1;
    private String mLine2;

    public itemData(int mImage, String mLine1, String mLine2) {
        this.mImage = mImage;
        this.mLine1 = mLine1;
        this.mLine2 = mLine2;
    }

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    public String getmLine1() {
        return mLine1;
    }

    public void setmLine1(String mLine1) {
        this.mLine1 = mLine1;
    }

    public String getmLine2() {
        return mLine2;
    }

    public void setmLine2(String mLine2) {
        this.mLine2 = mLine2;
    }
}
