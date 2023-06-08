package com.example.fu.data.repository

import com.example.fu.data.network.ModelApi
import com.example.fu.model.Disease
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ModelRepository @Inject constructor(
    private val modelApi: ModelApi,
    private val diseaseRepository: DiseaseRepository
) {

    suspend fun classifyDisease(file: File): ClassificationResult {
        try {
            val response = modelApi.classifyDisease(
                image = MultipartBody.Part
                    .createFormData(
                        "image",
                        file.name,
                        file.asRequestBody()
                    )
            )

            val value = response.predictedClass?.toIntOrNull()

            return if (value == null) {
                ClassificationResult.Error("")
            } else if (value == 1) {
                ClassificationResult.Healthy
            } else {
                val disease = findDisease(value)

                if (disease != null)
                    ClassificationResult.Success(disease)
                else
                    ClassificationResult.Error("")
            }
        } catch (e: IOException) {
            return ClassificationResult.Error(e.message.toString())
        } catch (e: HttpException) {
            return ClassificationResult.Error(e.message.toString())
        }
    }


    private suspend fun findDisease(value: Int): Disease? {
        return when(value) {
            0 -> diseaseRepository.getDiseaseById(2)
            2 -> diseaseRepository.getDiseaseById(1)
            3 -> diseaseRepository.getDiseaseById(3)
            else -> null
        }
    }
}

sealed class ClassificationResult {

    data class Success(val disease: Disease): ClassificationResult()

    data class Error(val message: String): ClassificationResult()

    object Healthy: ClassificationResult()

}