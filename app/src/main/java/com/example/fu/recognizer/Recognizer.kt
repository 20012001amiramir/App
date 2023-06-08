package com.example.fu.recognizer

import android.net.Uri
import androidx.databinding.Observable
import com.example.fu.recognizer.model.Recognized

interface Recognizer {
    fun recognize(imageUri: Uri): Observable
}