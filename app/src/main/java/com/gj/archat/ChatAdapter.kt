package com.gj.archat

import android.content.Context
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 *Created by GJ
 *on 2018/11/7 -上午 11:03
 */
class ChatAdapter(var context: Context, dataList: ArrayList<MsgBean>) : BaseMultiItemQuickAdapter<MsgBean, BaseViewHolder>(dataList) {
    init {
        addItemType(0, R.layout.item_msg_left)
        addItemType(1, R.layout.item_msg_right)
    }

    override fun convert(helper: BaseViewHolder, item: MsgBean?) {
        item?.let {
            helper.setText(R.id.UI_MsgText, item.msg)
        }
    }

}