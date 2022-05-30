package com.helowin.sdk.cache

import org.litepal.crud.LitePalSupport

class DevBean: LitePalSupport(){
    var address:String?=null
    var name:String?=null
    override fun equals(other: Any?): Boolean {
        if(other is DevBean)
        name?.let {
            if(other.name!=null){
                return it == other.name
            }
        }
        return false
    }
}