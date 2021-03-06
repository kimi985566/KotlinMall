package com.kotlin.user.injection.component

import com.kotlin.base.injection.PreComponentScope
import com.kotlin.base.injection.component.ActivityComponent
import com.kotlin.user.injection.module.UploadModule
import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.ui.activity.*
import dagger.Component

/**
 * @Component
 *
 * 注入器，连接目标和依赖实例的桥梁
 * 以@Component标注的类必须是接口或者抽象类
 * Component依赖关系通过dependencies属性添加
 * App必须有一个Component用来管理全局实例
 *
 * dependence实现方式总结：
 * 1. 父Component中要显式的写出需要暴露可提供给子Component的依赖；
 * 2. 子Component在注解中使用dependencies=来连接父Component；
 * 3. 注意子Component实例化方式。
 *
 * 在完成前需要先编译生成，否则不会生成编译文件
 * */

@PreComponentScope
@Component(modules = [(UserModule::class), (UploadModule::class)], dependencies = [(ActivityComponent::class)])
interface UserComponent {

    fun inject(activity: RegisterActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: ForgetPwdActivity)
    fun inject(activity: ResetPwdActivity)
    fun inject(activity: UserInfoActivity)

}