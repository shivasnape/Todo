package com.shivichu.passwordsaver.retrofit.base

import com.shivichu.passwordsaver.retrofit.base.MoreInfo


data class Error(
    var ErrorCode: String,
    var ErrorDescription: String,
    var MoreInfo: MoreInfo,
    var httpcode: Int
)