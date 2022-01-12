package com.application.onovapplication.activities.politicians

import com.application.onovapplication.model.ArticleModel

interface ParserResponseInterface {
    fun onParsingDone(articleModel: ArticleModel?)
}