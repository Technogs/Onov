package com.application.onovapplication.model

class ArticleModel(private var headline: String,private var article: String) {
//    private var headline: String? = null
//    private var article: String? = null

//    fun ArticleModel(headline: String?, article: String?) {
//        this.headline = headline
//        this.article = article
//    }

    fun getHeadline(): String {
        return headline
    }

    fun getArticle(): String {
        return article
    }
}