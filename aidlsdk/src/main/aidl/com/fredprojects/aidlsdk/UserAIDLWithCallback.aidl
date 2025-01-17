// UserAIDLWithCallback.aidl
package com.fredprojects.aidlsdk;

import com.fredprojects.aidlsdk.models.UserInfo;
import com.fredprojects.aidlsdk.utils.ResultCallback;

interface UserAIDLWithCallback {
    oneway void getUserInfo(in ResultCallback callback);
    oneway void setUserInfo(in UserInfo userInfo, in ResultCallback callback);
}