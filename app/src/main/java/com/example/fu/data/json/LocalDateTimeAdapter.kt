package com.example.fu.data.json

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.*

class LocalDateTimeAdapter : JsonAdapter<LocalDateTime>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun fromJson(reader: JsonReader): LocalDateTime? {
        val unixTimestamp = reader.nextLong()
        return Instant
            .ofEpochSecond(unixTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        writer.value(value?.toEpochSecond(ZoneId.systemDefault().rules.getOffset(value)))
    }
}