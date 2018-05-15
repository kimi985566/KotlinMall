package com.kotlin.user.service.impl

import com.kotlin.base.data.protocol.BaseResp
import com.kotlin.base.rx.BaseException
import com.kotlin.user.data.repository.UserRepository
import com.kotlin.user.service.UserService
import rx.Observable
import rx.functions.Func1

class UserServiceImpl : UserService {
    override fun register(mobile: String, verifyCode: String, pwd: String): Observable<Boolean> {
        val repository = UserRepository()

        return repository.register(mobile, pwd, verifyCode)
                .flatMap(Func1<BaseResp<String>, Observable<Boolean>> { t ->
                    if (t.status == 0) {
                        return@Func1 Observable.error(BaseException(t.status, t.message))
                    }
                    Observable.just(true)
                })


    }
}