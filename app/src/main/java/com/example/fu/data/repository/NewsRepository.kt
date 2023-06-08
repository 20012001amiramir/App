package com.example.fu.data.repository

import com.example.fu.model.Article
import com.example.fu.model.Disease
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import javax.inject.Inject

class NewsRepository @Inject constructor() {

    suspend fun getHeadlineArticles(): List<Article> {
        return articles
    }

    fun getHeadlineArticlesFlow() = flow { emit(articles) }

    suspend fun getArticleById(id: Int): Article {
        val article = articles.find { article -> article.id == id }
        return article ?: articles[0]
    }

    companion object {
        val articles = listOf(
            Article(
                id = 0,
                title = "Желтая ржавчина убивает",
                content = "Всего за две недели умерло 2 гектара поля",
                urlToImages = listOf(
                    "https://betaren.ru/upload/iblock/9b3/9b34029eadbf5217f3b4d02bac559ec3.JPG"
                )
            ),
            Article(
                id = 1,
                title = "Септориоз, как излечить?",
                content = "Всего 2-3 недели до того как...",
                urlToImages = listOf(
                    "https://glavagronom.ru/media/uploads/CeIL9vqwz-%D1%81%D0%B5%D0%BF%D1%82%D0%BE%D1%80%D0%B8%D0%BE%D0%B7.jpg"
                )
            ),
            Article(
                id = 2,
                title = "Желтая ржавчина убивает",
                content = "Всего за две недели умерло 2 гектара поля",
                urlToImages = listOf(
                    "https://betaren.ru/upload/iblock/9b3/9b34029eadbf5217f3b4d02bac559ec3.JPG"
                )
            ),
        )
    }

}