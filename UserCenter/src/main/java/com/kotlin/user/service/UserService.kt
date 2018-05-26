package com.kotlin.user.service

import com.kotlin.user.data.protocol.UserInfo
import rx.Observable

interface UserService {
    //用户注册
    fun register(mobile: String, pwd: String, verifyCode: String): Observable<Boolean>

    //用户登陆
    fun login(mobile: String, pwd: String, pushId: String): Observable<UserInfo>
}