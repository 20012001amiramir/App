package com.example.fu.data.repository

import com.example.fu.model.Category
import kotlinx.coroutines.delay
import javax.inject.Inject

class CategoryRepository @Inject constructor() {

    suspend fun getCategories(): List<Category> {
        return categories
    }

    suspend fun getCategoryById(id: Int): Category {
        val category = categories.find { category -> category.id == id }
        return category ?: categories[0]
    }

    companion object {

        private val categories = listOf(
            Category(
                id = 0,
                plantName = "Пшеница",
                countOfDiseases = 4,
                urlToImage = "https://betaren.ru/upload/iblock/9b3/9b34029eadbf5217f3b4d02bac559ec3.JPG"
            ),
            Category(
                id = 1,
                plantName = "Пшеница",
                countOfDiseases = 4,
                urlToImage = "https://betaren.ru/upload/iblock/9b3/9b34029eadbf5217f3b4d02bac559ec3.JPG"
            ),
            Category(
                id = 2,
                plantName = "Пшеница",
                countOfDiseases = 4,
                urlToImage = "https://betaren.ru/upload/iblock/9b3/9b34029eadbf5217f3b4d02bac559ec3.JPG"
            ),
            Category(
                id = 3,
                plantName = "Пшеница",
                countOfDiseases = 4,
                urlToImage = "https://betaren.ru/upload/iblock/9b3/9b34029eadbf5217f3b4d02bac559ec3.JPG"
            ),
        )

    }

}