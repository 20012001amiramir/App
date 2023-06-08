package com.example.fu.di.provider

import com.example.fu.data.json.LocalDateAdapter
import com.squareup.moshi.Moshi
import com.example.fu.data.json.LocalDateTimeAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Provider

class MoshiProvider @Inject constructor() : Provider<Moshi> {

    override fun get(): Moshi =
        Moshi
            .Builder()
            .add(LocalDate::class.java, LocalDateAdapter().nullSafe())
            .add(LocalDateTime::class.java, LocalDateTimeAdapter().nullSafe())
            .build()
}