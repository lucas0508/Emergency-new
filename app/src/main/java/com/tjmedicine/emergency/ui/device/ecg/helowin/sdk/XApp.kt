package com.helowin.sdk

import com.helowin.sdk.cache.XCache
import org.litepal.LitePalApplication

class XApp:LitePalApplication(){
    override fun onCreate() {
        super.onCreate()
        XCache.init(this)


    }
    companion object{
        const val channel:String = "re1212021"
        const val update_file_url ="https://api-test.995120.cn/mini/file/upload"
        const val oauth_url =
            "https://api-test.995120.cn/oauth/token?grant_type=client_credentials&client_id=re1212021&client_secret=re1212021"

        const val url =
            "http://api-test.995120.cn/mini/api/"
    }

}