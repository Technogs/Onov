package com.application.onovapplication.fragments

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.activities.politicians.ParserResponseInterface
import com.application.onovapplication.databinding.ConstitutionLayoutBinding
import com.application.onovapplication.model.ArticleModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class ConstitutionFragment: BaseFragment(),ParserResponseInterface {

    lateinit var binding: ConstitutionLayoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ConstitutionLayoutBinding.inflate(inflater, container, false)
//        WebScratch().execute()
//        HtmlParser(this).execute("https://constitutionus.com/");
//        binding.btnView.setOnClickListener {}
//        binding.textView.setText(getString(R.string.nice_html))
        binding.textView.setText(Html.fromHtml(getString(R.string.nice_html), FROM_HTML_MODE_LEGACY));

        return binding.getRoot()
    }
    class HtmlParser(parserResponseInterface: ParserResponseInterface) :
        AsyncTask<String?, Void?, ArticleModel?>() {
        private val parserResponseInterface: ParserResponseInterface
         override fun doInBackground(vararg params: String?): ArticleModel? {
            val url = params[0]
            var articleModel: ArticleModel? = null
            val headline: String
            var article = ""
            val pageDocument: Document
            val elements: Elements
            val articleElements: Elements
            try {
                pageDocument = Jsoup.connect(url).get()
                elements = pageDocument.select("#body-content")
                articleElements = pageDocument.select(".wrap .cols .col-1of2 p")
                headline = elements.select("h1").text()
                for (element in articleElements) {
                    article = """
                    $article${element.text()}
                    
                    
                    """.trimIndent()
                }
                articleModel = ArticleModel(headline, article)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return articleModel
        }

        override fun onPostExecute(articleModel: ArticleModel?) {
            super.onPostExecute(articleModel)
            parserResponseInterface.onParsingDone(articleModel)
        }

        init {
            this.parserResponseInterface = parserResponseInterface
        }
    }

    override fun onParsingDone(articleModel: ArticleModel?) {
        binding.progressBar.setVisibility(View.GONE)
        if (articleModel != null) {
            binding.headline.setText(articleModel.getHeadline())
            binding.article.setText(articleModel.getArticle())
        } else binding.errorMessage.setText("Something wrong! Can't parse HTML")
    }
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private lateinit var words: String
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect("https://constitutionus.com/").get()
                words = document.text()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            binding.textView.text = Html.fromHtml(words, FROM_HTML_MODE_LEGACY)
//            binding.textView.text = words
        }
    }
}