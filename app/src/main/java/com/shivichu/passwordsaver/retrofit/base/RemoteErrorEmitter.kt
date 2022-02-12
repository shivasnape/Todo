package com.shivichu.passwordsaver.retrofit.base

import com.shivichu.passwordsaver.retrofit.base.ErrorType

interface RemoteErrorEmitter {
    fun onError(errorType: ErrorType, msg: String)
}