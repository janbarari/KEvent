package io.github.janbarari.kevent

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class DefaultEvent : Serializable {
    private var sender: Any
    private var data = HashMap<String, Any>()

    constructor(sender: Any) {
        this.sender = sender
    }

    private constructor(sender: Any, data: HashMap<String, Any>) {
        this.sender = sender
        this.data = data
    }

    fun getSender(): Any {
        return sender
    }

    fun getRawHashMap(): HashMap<String, Any> {
        return data
    }

    fun containsKey(key: String): Boolean {
        return data.containsKey(key)
    }

    fun put(key: String, value: Any): DefaultEvent {
        data[key] = value
        return DefaultEvent(sender, data)
    }

    operator fun get(key: String): Any {
        return data[key]!!
    }

    fun getString(key: String): String {
        return data[key] as String
    }

    fun getBoolean(key: String): Boolean {
        return data[key] as Boolean
    }

    fun getInt(key: String): Int {
        return data[key] as Int
    }

    fun getLong(key: String): Long {
        return data[key] as Long
    }

    fun getDouble(key: String): Double {
        return data[key] as Double
    }

    fun getParcelable(key: String): Parcelable {
        return data[key] as Parcelable
    }

    fun getFloat(key: String): Float {
        return data[key] as Float
    }

    fun getBundle(key: String): Bundle {
        return data[key] as Bundle
    }

    fun getChar(key: String): Char {
        return data[key] as Char
    }

    override fun toString(): String {
        return String.format("sender:%s;data:%s", sender::class.java.name, data.toString())
    }
}
