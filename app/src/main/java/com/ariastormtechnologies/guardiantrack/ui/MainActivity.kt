package com.ariastormtechnologies.guardiantrack.ui

// Importación de bibliotecas necesarias
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ariastormtechnologies.guardiantrack.R
import com.ariastormtechnologies.guardiantrack.ui.Fragment.AlertFragment
import com.ariastormtechnologies.guardiantrack.ui.Fragment.ConfigurationFragment
import com.ariastormtechnologies.guardiantrack.ui.Fragment.MapsFragment
import com.ariastormtechnologies.guardiantrack.ui.Fragment.SearchFragment
import com.ariastormtechnologies.guardiantrack.ui.Fragment.EarthquakesFragment

// Definición de la actividad principal de la aplicación
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    companion object {
        var GLOBAL_USER_ID: String? = "687c6679886044c43b1483ea" // Reemplaza por un userId real
    }

    // Método que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Establece el diseño de la actividad

        webView = findViewById(R.id.ButtonsPanel)
        webView.setBackgroundColor(Color.TRANSPARENT) // Fondo transparente
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.settings.domStorageEnabled = true
        //webView.addJavascriptInterface(WebAppInterface(this), "Android")
        webView.addJavascriptInterface(WebAppInterface(this, supportFragmentManager), "Android")
        webView.loadUrl("file:///android_asset/menu.html")

        // Verifica si es la primera vez que se crea la actividad
        if (savedInstanceState == null) {
            // Crea una instancia del fragmento MapsFragment
            val mapsFragment = MapsFragment()

            // Inicia una transacción de fragmentos
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, mapsFragment) // Reemplaza el contenedor de fragmentos con MapsFragment
                .commit() // Confirma la transacción

            // Agrega un listener para detectar cambios en el stack de fragmentos
            supportFragmentManager.addOnBackStackChangedListener {
                // Obtiene el fragmento actual visible
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                // Si el fragmento actual es MapsFragment, desactiva el botón SOS
                if (currentFragment is MapsFragment) {
                    currentFragment.disableSOSButton()
                }
            }
        }


    }

    class WebAppInterface(private val context: Context, private val supportFragmentManager: FragmentManager) {

        @JavascriptInterface
        fun onMenuClick(index: Int) {
            Log.d("WebView", "Botón clicado: $index")
            // Aquí puedes cambiar el fondo, abrir fragmentos, etc.
            when (index) {
                0 -> (context as AppCompatActivity).runOnUiThread {
                    context.findViewById<View>(R.id.root_layout).setBackgroundColor(
                        Color.parseColor("#ffb457")
                    )

                    Log.d("Menu", "IRIS seleccionado")
                    val tag = "NaturaldisasterFragment"
                    context.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, EarthquakesFragment(), tag)
                        .addToBackStack(tag)
                        .commit()
                }

                1 -> (context as AppCompatActivity).runOnUiThread {
                    context.findViewById<android.view.View>(R.id.root_layout).setBackgroundColor(Color.TRANSPARENT)

                    Log.d("Menu", "search seleccionadas")
                    val tag = "ReportsFragment"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, SearchFragment(), tag)
                        .addToBackStack(tag)
                        .commit()
                }
                2 -> (context as AppCompatActivity).runOnUiThread {
                    context.findViewById<android.view.View>(R.id.root_layout).setBackgroundColor(Color.TRANSPARENT)

                    Log.d("Menu", "Emitir SOS seleccionada")

                    val tag = "MapsFragment"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, MapsFragment(), tag)
                        .addToBackStack(tag)
                        .commit()

                    // Esperamos a que el fragmento sea visible antes de pedir habilitar el botón
                    supportFragmentManager.executePendingTransactions()
                    val maps = supportFragmentManager.findFragmentByTag(tag) as? MapsFragment
                    maps?.let {
                        it.enableSOSButton() // Vista ya inicializada
                    }
                }
                3 -> (context as AppCompatActivity).runOnUiThread {
                    context.findViewById<android.view.View>(R.id.root_layout).setBackgroundColor(Color.TRANSPARENT)

                    Log.d("Menu", "Alertas seleccionadas")
                    val tag = "AlertFragment"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, AlertFragment(), tag)
                        .addToBackStack(tag)
                        .commit()
                }
                4 -> (context as AppCompatActivity).runOnUiThread {
                    context.findViewById<android.view.View>(R.id.root_layout).setBackgroundColor(Color.TRANSPARENT)

                    Log.d("Menu", "Ajustes seleccionadas")
                    val tag = "ConfigurationFragment"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ConfigurationFragment(), tag)
                        .addToBackStack(tag)
                        .commit()
                }

            }
        }
    }
}
