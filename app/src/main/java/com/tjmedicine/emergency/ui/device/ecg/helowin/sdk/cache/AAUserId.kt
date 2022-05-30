package com.helowin.sdk.cache

import org.litepal.LitePal
import org.litepal.crud.LitePalSupport

class AAUserId : LitePalSupport(){
    var id_:String?=null
    var uid:String?=null
    override fun equals(other: Any?): Boolean {
        if(other is AAUserId)
            id_?.let {
                if(other.id_!=null){
                    return it == other.id_
                }
            }
        return false
    }
    fun saveUpdate():Boolean{
        return saveOrUpdate("id_ = ?",id_)
    }
}
fun String.findUid():String?{
    var t = LitePal.where("id_ = ?",this).findFirst(AAUserId::class.java)
    if(t!=null){
        return t.uid
    }
    return null
}