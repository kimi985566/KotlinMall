package com.kotlin.user.injection.component

import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.ui.activity.RegisterActivity
import dagger.Component

/**
 * @Component
 *
 * 注入器，连接目标和依赖实例的桥梁
 * 以@Component标注的类必须是接口或者抽象类
 * Component依赖关系通过dependencies属性添加
 * App必须有一个Component用来管理全局实例
 *
 * 在完成前需要先编译生成，否则没有对应方法生成
 * */

@Component(modules = [(UserModule::class)])
interface UserComponent {

    fun inject(activity: RegisterActivity)

}