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

        binding.textView.setText(Html.fromHtml(getString(R.string.nice_html), FROM_HTML_MODE_LEGACY))

        return binding.getRoot()
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

        }
    }
}