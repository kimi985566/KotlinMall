package com.ycy.goods.injection.component

import com.kotlin.base.injection.PreComponentScope
import com.kotlin.base.injection.component.ActivityComponent
import com.ycy.goods.injection.module.CategoryModule
import com.ycy.goods.ui.fragment.CategoryFragment
import dagger.Component


/*
    商品分类Component
 */
@PreComponentScope
@Component(dependencies = [(ActivityComponent::class)], modules = [(CategoryModule::class)])
interface CategoryComponent {
    fun inject(fragment: CategoryFragment)
}
