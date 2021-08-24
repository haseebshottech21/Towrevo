package com.Towrevo.util.extension

import com.google.gson.Gson

fun <E> MutableList<E>.arrayToJsonString(): String {
    return Gson().toJson(this)
}
fun <E> Gson.modelToJsonString(): String {
    return Gson().toJson(this)
}
@JvmName("arrayToJsonStringE")
fun <E> List<E>.arrayToJsonString(): String {
    return Gson().toJson(this)

}
fun <T> getSubList(list: List<T>, start: Int, end: Int): List<T>? {
    return list.subList(start, end + 1)
}
