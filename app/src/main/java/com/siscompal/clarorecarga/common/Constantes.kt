package com.siscompal.clarorecarga.common

import android.os.Build
import com.siscompal.clarorecarga.BuildConfig

class Constantes {
    companion object{
        const val HOST:String="distrirecarga.co"
        const val PUERTO:Int=8888
        const val HMAC_MD5_ALGORITHM = "HmacMD5"
        const val VERSION_CODE= BuildConfig.VERSION_CODE;
    }
}