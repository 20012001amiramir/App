package com.example.fu.data.json

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class LocalDateAdapter : JsonAdapter<LocalDate>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun fromJson(reader: JsonReader): LocalDate? {
        val unixTimestamp = reader.nextLong()
        return Instant
            .ofEpochSecond(unixTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        writer.value(
            value
                ?.atStartOfDay(ZoneId.systemDefault())
                ?.toEpochSecond()
        )
    }
}