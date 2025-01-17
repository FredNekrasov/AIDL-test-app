// UserAIDL.aidl
package com.fredprojects.aidlsdk;

import com.fredprojects.aidlsdk.models.UserInfo;

interface UserAIDL {
    UserInfo getUserInfo();
    oneway void setUserInfo(in UserInfo userInfo);
}