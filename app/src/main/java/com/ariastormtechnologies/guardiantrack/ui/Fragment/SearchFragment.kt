package com.ariastormtechnologies.guardiantrack.ui.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ariastormtechnologies.guardiantrack.R
import com.ariastormtechnologies.guardiantrack.network.RetrofitClient
import com.google.gson.Gson


class SearchFragment : Fragment() {

    private lateinit var listPersonMissing: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_database, container, false)
        listPersonMissing = view.findViewById(R.id.ListPersonMissing)

        val webSettings: WebSettings = listPersonMissing.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true // Esto habilita localStorage/sessionStorage

        listPersonMissing.settings.javaScriptEnabled = true
        listPersonMissing.webChromeClient = WebChromeClient()
        //listPersonMissing.webViewClient = WebViewClient()

        listPersonMissing.loadUrl("file:///android_asset/listpersonmissing.html")

        // Carga los datos cuando la vista esté lista
        listPersonMissing.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadMissingPeople()
            }
        }

        return view

        /*
        {
   "_id":{
      "$oid":"68b2236b143b867459b0d363"
   },
   "folio":"ALBA-JAL-2025-001",
   "full_name":"Laura Ramirez",
   "age":{
      "$numberInt":"29"
   },
   "gender":"F",
   "state":"Jalisco",
   "city":"Guadalajara",
   "missing_date":{
      "$date":{
         "$numberLong":"1749686400000"
      }
   },
   "protocol":"Alba Protocol",
   "status":"Missing",
   "condition":"",
   "photo_urls":[
      "https://randomuser.me/api/portraits/women/50.jpg",
      "https://randomuser.me/api/portraits/women/44.jpg",
      "https://randomuser.me/api/portraits/women/45.jpg",
      "https://randomuser.me/api/portraits/women/46.jpg",
      "https://randomuser.me/api/portraits/women/47.jpg"
   ],
   "description":"Wearing blue jeans, white t-shirt, tattoo on right arm.",
   "created_at":{
      "$date":{
         "$numberLong":"1756306800000"
      }
   },
   "__v":{
      "$numberInt":"0"
   },
   "complexion":"",
   "notes":"",
   "reporter":""
}
        */
    }

    private fun loadMissingPeople() {
        lifecycleScope.launchWhenStarted {
            try {
                val response = RetrofitClient.apiService.getMissingPeople()
                if (response.isSuccessful) {
                    val peopleList = response.body() ?: emptyList()
                    val json = Gson().toJson(peopleList)
                    println("Datos Json: " + json)
                    // Inyecta los datos al WebView
                    listPersonMissing.evaluateJavascript("showPeople($json);", null)
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", "Error loading missing people", e)
            }
        }
    }
}