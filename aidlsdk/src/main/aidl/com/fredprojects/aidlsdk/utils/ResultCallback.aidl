// ResultCallback.aidl
package com.fredprojects.aidlsdk.utils;

// Declare any non-default types here with import statements
import com.fredprojects.aidlsdk.models.UserInfo;

interface ResultCallback {
    void onSuccess(in UserInfo info);
    void onError(in String errorMessage);
}