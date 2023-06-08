package com.example.fu.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fu.data.network.Api
import com.example.fu.data.network.request.AddGarbageRequest
import com.example.fu.data.network.response.AddGarbageResponse
import com.example.fu.data.network.response.GarbagesResponse
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.domain.profile.AccountInfo
import com.example.fu.util.fromJson
import com.example.fu.util.toJson
import com.example.fu.util.valueAsUnique
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
class GarbageRepository @Inject constructor(
    private val api: Api,
    private val localStorage: LocalStorage
) {

    private val garbageFlow = MutableStateFlow<AddGarbageResponse?>(null)

    fun getGarbageFlow(): Flow<AddGarbageResponse> = garbageFlow.filterNotNull()

    suspend fun loadGarbageTypes(barcode: String) {
        garbageFlow.valueAsUnique = api.GetGarbageInfo(barcode)
    }

    private val garbagesScanedFlow = MutableStateFlow<GarbagesResponse?>(null)

    fun getGarbagesScanedByAuthorizedUserFlow(): Flow<GarbagesResponse> = garbagesScanedFlow.filterNotNull()

    suspend fun loadGarbagesScanedInfo() {
        garbagesScanedFlow.valueAsUnique = api.GetGarbagesScanedByAuthorizedUser()
    }

    private val addGarbageFlow = MutableStateFlow<AddGarbageResponse?>(null)

    fun getAddGarbageFlow(): Flow<AddGarbageResponse> = addGarbageFlow.filterNotNull()

    suspend fun addGarbage(garbage: AddGarbageRequest) {
        addGarbageFlow.valueAsUnique = api.AddGarbageInfo(garbage.name, garbage.garbageTypes, garbage.barcode,garbage.image)
    }


}