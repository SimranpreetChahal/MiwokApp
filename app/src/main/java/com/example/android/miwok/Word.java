package com.example.android.miwok;

/**
 * Created by simranpreetkaurchahal on 2016-12-20.
 */

public class Word {

    private String mDefaultTraslation;
    private String mMiwokTranslation;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private int mMediaFileResourceId;
    private static final int NO_IMAGE_PROVIDED = -1;

    //constructors
    public Word(String defaultTraslation,String mewokTranslation,int resId, int mediaResourceId) {
        mDefaultTraslation = defaultTraslation;
        mMiwokTranslation = mewokTranslation;
        mImageResourceId = resId;
        mMediaFileResourceId = mediaResourceId;
    }

    public Word(String defaultTraslation,String mewokTranslation,int mediaResourceId) {
        mDefaultTraslation = defaultTraslation;
        mMiwokTranslation = mewokTranslation;
        mMediaFileResourceId = mediaResourceId;
    }

    //getters
    public String getmDefaultTraslation() {
        return mDefaultTraslation;
    }
    public String getMewokTranslation() {
        return mMiwokTranslation;
    }
    public int getImageResourceId() { return mImageResourceId;}
    public int getMediaFileResourceId() { return mMediaFileResourceId;}
    public boolean hasImage() { return mImageResourceId != NO_IMAGE_PROVIDED; }
}
