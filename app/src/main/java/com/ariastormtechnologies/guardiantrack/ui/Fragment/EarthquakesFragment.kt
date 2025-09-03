package com.ariastormtechnologies.guardiantrack.ui.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import com.ariastormtechnologies.guardiantrack.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class EarthquakesFragment : Fragment() {

    private lateinit var webViewSismo: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_earthquakes_fragment, container, false)
        webViewSismo = view.findViewById(R.id.webViewSismo)

        webViewSismo.settings.javaScriptEnabled = true

        webViewSismo.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                getEarthquakeData { json ->
                    Log.d("JS_DATA", json)
                    view?.post {
                        view.evaluateJavascript("updateData($json);", null)
                    }
                }
            }
        }

        // Esto permite ver errores de JS en Logcat
        webViewSismo.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d(
                    "WebViewConsole",
                    "${consoleMessage?.message()} -- line ${consoleMessage?.lineNumber()}"
                )
                return true
            }
        }

        webViewSismo.loadUrl("file:///android_asset/sismo.html")
        return view
    }
//.url("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_hour.geojson")
    //.url("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.geojson")
    //.url("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_week.geojson")

    private fun getEarthquakeData(onResult: (String) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_week.geojson")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val json = responseBody.string()
                    val jsonObj = JSONObject(json)
                    val features = jsonObj.getJSONArray("features")

                    if (features.length() > 0) {
                        val first = features.getJSONObject(0)
                        val props = first.getJSONObject("properties")
                        val geometry = first.getJSONObject("geometry").getJSONArray("coordinates")

                        val mag = props.getDouble("mag")
                        val place = props.getString("place")
                        val time = props.getLong("time")
                        val depth = geometry.getDouble(2)
                        val lon = geometry.getDouble(0)
                        val lat = geometry.getDouble(1)

                        val alert = props.optString("alert", "sin datos")
                        val tsunami = props.optInt("tsunami", 0)
                        val felt = props.optInt("felt", 0)
                        val url = props.optString("url", "")

                        val jsData = """
                        {
                            "mag": $mag,
                            "place": "${place.replace("\"", "\\\"")}",
                            "depth": $depth,
                            "lat": $lat,
                            "lon": $lon,
                            "time": $time,
                            "alert": "$alert",
                            "tsunami": $tsunami,
                            "felt": $felt,
                            "url": "$url"
                        }
                    """.trimIndent()

                        activity?.runOnUiThread {
                            onResult(jsData)
                        }
                    }
                }
            }
        })
    }

}
