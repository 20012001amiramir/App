package com.example.fu.util

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.reflect.Type

val defaultGson: Gson by lazy { Gson() }

/**
 * Tries to parse object of class [classOfT] from given [json]
 * @return parsed object or null in case of parsing error
 */
fun <T> Gson.fromJsonOrNull(json: String, classOfT: Class<T>): T? = try {
    fromJson(json, classOfT)
} catch (e: JsonParseException) {
    null
}

/**
 * Tries to parse object of type [typeOfT] from given [json]
 * @return parsed object or null in case of parsing error
 */
fun <T> Gson.fromJsonOrNull(json: String, typeOfT: Type): T? = try {
    fromJson(json, typeOfT)
} catch (e: JsonParseException) {
    null
}


/**
 * Parses object of generic type [T], which can be a regular or generic class (e.g. List<String>)
 * @throws JsonParseException in case of parsing error
 */
inline fun <reified T> Gson.fromJson(json: String): T =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)

/**
 * Parses object of generic type [T], which can be a regular or generic class (e.g. List<String>)
 * @throws JsonParseException in case of parsing error
 */
inline fun <reified T> Gson.fromJson(reader: Reader): T =
    this.fromJson<T>(reader, object : TypeToken<T>() {}.type)

/**
 * Tries to parse object of type [T], which can be a regular or generic class (e.g. List<String>)
 * @return parsed object or null in case of parsing error
 */
inline fun <reified T> Gson.fromJsonOrNull(json: String): T? =
    this.fromJsonOrNull<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Reader.fromJson(): T = defaultGson.fromJson(this)

inline fun <reified T> String.fromJson(): T = defaultGson.fromJson(this)

inline fun <reified T> String.fromJsonOrNull(): T? = defaultGson.fromJsonOrNull(this)

fun <T> String.fromJson(typeOfT: Type): T = defaultGson.fromJson(this, typeOfT)

fun <T> String.fromJson(classOfT: Class<T>): T = defaultGson.fromJson(this, classOfT)

fun <T> String.fromJsonOrNull(typeOfT: Type): T? = defaultGson.fromJsonOrNull(this, typeOfT)

fun <T> String.fromJsonOrNull(classOfT: Class<T>): T? = defaultGson.fromJsonOrNull(this, classOfT)


fun Any.toJson(): String = defaultGson.toJson(this)
