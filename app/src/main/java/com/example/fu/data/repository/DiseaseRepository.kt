package com.example.fu.data.repository

import com.example.fu.model.Disease
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import javax.inject.Inject

class DiseaseRepository @Inject constructor() {

    suspend fun getFrequentDisease(): List<Disease> {
        return diseases
    }

    fun getDiseasesFlow() = flow { emit(diseases) }

    suspend fun getDiseaseById(id: Int): Disease {
        val disease = diseases.find { disease -> disease.id == id }
        return disease ?: diseases[0]
    }

    suspend fun getDiseaseByPlantName(name: String): List<Disease> {
        return diseases.filter { disease -> disease.diseaseName != name }
    }

    companion object {
        private val diseases = listOf(
            Disease(
                id = 0,
                plantName = "Пшеница",
                diseaseName = "Желтая Ржавчина",
                sourceName = "Из Википедии - свободной энциклопедии",
                description = "Желтая ржавчина, или полосатая ржавчина, получила свое название от появления полос желтого цвета, образующихся параллельно вдоль жилков каждой листовой пластинки. Эти желтые полосы на самом деле...",
                urlToImages = listOf(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Wheat_leaf_rust_on_wheat.jpg/275px-Wheat_leaf_rust_on_wheat.jpg",
                    "https://betaren.ru/upload/iblock/d31/d3127913eb1d009d728c599bc1325428.jpg",
                    "https://wikigrib.ru/images/Puccinia-recondita.jpg",
                    "https://www.syngenta.ru/sites/g/files/kgtney371/files/styles/main_media_large/public/media/image/2016/06/06/pests.brown-rust-of-wheat.02.jpg?itok=reorhVQL",
                    "https://www.crop-protection.ru/upload_files/image/bolezni-rasteniy/rzhavchina/rzhavchina-pshenicy3.jpg"
                ),
                urlToSource = "https://ru.wikipedia.org/wiki/%D0%91%D1%83%D1%80%D0%B0%D1%8F_%D1%80%D0%B6%D0%B0%D0%B2%D1%87%D0%B8%D0%BD%D0%B0_%D0%BF%D1%88%D0%B5%D0%BD%D0%B8%D1%86%D1%8B",
                danger = "Маленькая",
                symptoms = listOf("Локальные"),
                type = "Грибковая",
                treatment = "Фунигициды",
            ),
            Disease(
                id = 1,
                plantName = "Пшеница",
                diseaseName = "Септориоз",
                sourceName = "Из Википедии - свободной энциклопедии",
                description = "Септориоз – это грибковое заболевание, которое встречается как на культурных, так и на дикорастущих растениях. Из-за характерного оттенка и формы следов его называют бурой или белой пятнистостью. Для борьбы с болезнью применяют народные средства и химические фунгициды. ",
                urlToImages = listOf(
                    "https://upload.wikimedia.org/wikipedia/commons/b/be/Septoria.scabiosicola.-.lindsey.jpg"
                ),
                urlToSource = "https://ru.wikipedia.org/wiki/%D0%A1%D0%B5%D0%BF%D1%82%D0%BE%D1%80%D0%B8%D1%8F",
                danger = "Средняя",
                symptoms = listOf("Локальные"),
                type = "Грибковая",
                treatment = "Фунигициды",
            ),
            Disease(
                id = 2,
                plantName = "Пшеница",
                diseaseName = "Коричневая Ржавчина",
                sourceName = "Из Википедии - свободной энциклопедии",
                description = "Коричневая ржавчина, или полосатая ржавчина, получила свое название от появления полос желтого цвета, образующихся параллельно вдоль жилков каждой листовой пластинки. Эти желтые полосы на самом деле Read More",
                urlToImages = listOf(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Wheat_leaf_rust_on_wheat.jpg/275px-Wheat_leaf_rust_on_wheat.jpg"
                ),
                urlToSource = "https://ru.wikipedia.org/wiki/%D0%91%D1%83%D1%80%D0%B0%D1%8F_%D1%80%D0%B6%D0%B0%D0%B2%D1%87%D0%B8%D0%BD%D0%B0_%D0%BF%D1%88%D0%B5%D0%BD%D0%B8%D1%86%D1%8B",
                danger = "Маленькая",
                symptoms = listOf("Локальные"),
                type = "Грибковая",
                treatment = "Фунигициды",
            ),
        )
    }
}