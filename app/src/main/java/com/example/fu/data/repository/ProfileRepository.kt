package com.example.fu.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.model.Profile
import kotlinx.coroutines.flow.combine
import java.io.File
import java.util.Date
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val context: Context,
    private val localStorage: LocalStorage
) {

    val profile = combine(
            localStorage.profileImageFlow,
            localStorage.profileNameFlow
        ) { image, name ->
            Profile(
                name = name ?: "",
                imagePath = Uri.parse(image)
            )
        }

    fun updateName(name: String) {
        localStorage.profileName = name
    }

    fun updateImage(uri: Uri) {

        val filename = "${Date().time.toString(16)}.jpeg"
        val fileDir = context.cacheDir
        val file = File(fileDir, filename)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = context.contentResolver.openOutputStream(file.toUri())

        val bytes = inputStream?.readBytes()
        outputStream?.write(bytes)

        inputStream?.close()

        localStorage.profileImage = file.toUri().toString()

    }

}