package com.gj.archat

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 *Created by GJ
 *on 2018/11/7 -上午 10:27
 */
data class MsgBean(var msg: String, var direction: Int) : MultiItemEntity {
    override fun getItemType(): Int = direction
}