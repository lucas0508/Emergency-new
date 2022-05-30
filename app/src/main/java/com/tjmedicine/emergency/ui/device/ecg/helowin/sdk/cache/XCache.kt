package com.helowin.sdk.cache

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.text.TextUtils
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock
object XCache{
    fun getMac():String?{
        return getString("MAC",null)
    }
    fun getSn():String?{
        return getString("SN",null)
    }
    fun save(d:DevBean){
        edit().putString("MAC",d.address).putString("SN",d.name).commit()
    }



    fun save(name:String,mac:String): DevBean? {
        if(name != "NULL") {
            name.let {
                edit().putString(mac, it).putString(it, mac).commit()
            }
        }
        getString(mac,null)?.let {
          return DevBean().apply {
              this.address=mac
              this.name=it
          }
        }
        return null
    }


    fun init(c: Context){
        sp = c.getSharedPreferences("XCache",0)
    }
    private val rwl = ReentrantReadWriteLock()
    private val r: Lock = rwl.readLock()
    private val w: Lock = rwl.writeLock()
    private val cache = HashMap<String, String?>()
    private lateinit var sp: SharedPreferences
    fun getString(key: String, defValue: String?): String? {
        r.lock()
        return try {
            cache[key]?:sp.getString(key,null)?.apply {cache[key]=this}?:defValue
        } finally {
            r.unlock()
        }
    }
    fun getInt(key: String, defValue: Int): Int {
        val value = getString("I$key", null)
        return value?.toInt() ?: defValue
    }

    fun getLong(key: String, defValue: Long): Long {
        val value = getString("L$key", null)
        return value?.toLong() ?: defValue
    }

    fun getFloat(key: String, defValue: Float): Float {
        val value = getString("F$key", null)
        return value?.toFloat() ?: defValue
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val value = getInt("B$key", Int.MAX_VALUE)
        return if (value == Int.MAX_VALUE) defValue else value != 0
    }

    operator fun contains(key: String?): Boolean {
        return sp.contains(key)
    }

    fun edit(): CacheEditor{
        return CacheEditor()
    }
    class CacheEditor : Editor {
       private var editor = sp.edit()
        private var isChanged = false
        override fun putString(key: String, value: String?): Editor {
            if (TextUtils.isEmpty(value)) return remove(key)
            val oldValue: String? = getString(key,null)
            w.lock()
            try {
                if (oldValue == null || oldValue != value) {
                    cache.put(key,value)
                    editor.putString(key, value)
                    isChanged = true
                }
                 return  this
            } finally {
                w.unlock()
            }
        }
        override fun putStringSet(key: String, values: Set<String>?): Editor? {
            return null
        }

        override fun putInt(key: String, value: Int): Editor {
            return putString("I$key", value.toString())
        }

        override fun putLong(key: String, value: Long): Editor {
            return putString("L$key", value.toString())
        }

        override fun putFloat(key: String, value: Float): Editor {
            return putString("F$key", value.toString())
        }

        override fun putBoolean(key: String, value: Boolean): Editor {
            return putInt("B$key", if (value) 1 else 0)
        }

        override fun remove(key: String): Editor {
            w.lock()
            return try {
                cache.remove(key)
                editor.remove(key)
                isChanged = true
                this
            } finally {
                w.unlock()
            }
        }

        override fun clear(): Editor {
            w.lock()
            return try {
                cache.clear()
                editor.clear()
                isChanged = true
                this
            } finally {
                w.unlock()
            }
        }

        override fun commit(): Boolean {
            w.lock()
            try {
                if (isChanged) {
                    isChanged = false
                    return editor.commit()
                }
            } finally {
                w.unlock()
            }
            return false
        }

        override fun apply() {}
    }
}