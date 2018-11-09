package com.gj.archat

import com.blankj.utilcode.util.ToastUtils
import com.google.ar.core.exceptions.*
import com.google.ar.sceneform.ux.ArFragment

/**
 *Created by GJ
 *on 2018/11/7 -上午 9:41
 */
class ArFragmentCN : ArFragment() {
    override fun handleSessionException(sessionException: UnavailableException?) {
        ToastUtils.showLong(
            when (sessionException) {
                is UnavailableArcoreNotInstalledException -> "请安装ARCore"
                is UnavailableApkTooOldException -> "请升级ARCore"
                is UnavailableSdkTooOldException -> "请升级app"
                is UnavailableDeviceNotCompatibleException -> "当前设备部不支持AR"
                else -> {
                    "未能创建AR会话,请查看机型适配,arcore版本与系统版本${sessionException.toString()}"
                }
            }
        )
    }
}