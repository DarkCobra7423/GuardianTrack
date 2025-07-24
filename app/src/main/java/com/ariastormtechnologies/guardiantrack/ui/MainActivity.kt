package com.ariastormtechnologies.guardiantrack.ui

// Importación de bibliotecas necesarias
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.ariastormtechnologies.guardiantrack.R
import com.ariastormtechnologies.guardiantrack.ui.Fragment.AlertFragment
import com.ariastormtechnologies.guardiantrack.ui.Fragment.ConfigurationFragment
import com.ariastormtechnologies.guardiantrack.ui.Fragment.MapsFragment
import com.ariastormtechnologies.guardiantrack.ui.Fragment.ProfileFragment
import com.ariastormtechnologies.guardiantrack.ui.Fragment.SearchFragment

// Definición de la actividad principal de la aplicación
class MainActivity : AppCompatActivity() {

    // Método que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Establece el diseño de la actividad

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

        // Configura el listener para el ícono de avatar en la barra superior
        val avatar = findViewById<ImageView>(R.id.avatarIcon)
        avatar.setOnClickListener {
            // Crea un menú emergente asociado al ícono de avatar
            val popup = PopupMenu(this, avatar)
            popup.menuInflater.inflate(R.menu.profile_menu, popup.menu) // Infla el menú desde un recurso XML
            // 1️⃣ Asigna el listener ANTES de inflar
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_profile -> {
                        Log.d("Menu", "Perfil seleccionado")
                        val tag = "ProfileFragment"
                        /*val existing = supportFragmentManager.findFragmentByTag(tag)
                        if (existing == null) {*/
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, ProfileFragment(), "tag")
                            .addToBackStack(tag)
                            .commit()
                        /*} else {
                            Log.d("Menu", "El fragmento AlertFragment ya está cargado, no se reemplaza")
                        }*/

                        true
                    }
                    R.id.menu_sos -> {
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
                        true
                    }

                    R.id.menu_alert -> {
                        Log.d("Menu", "Alertas seleccionadas")
                        val tag = "AlertFragment"
                        /*val existing = supportFragmentManager.findFragmentByTag(tag)
                        if (existing == null) {*/
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer, AlertFragment(), tag)
                                .addToBackStack(tag)
                                .commit()
                        /*} else {
                            Log.d("Menu", "El fragmento AlertFragment ya está cargado, no se reemplaza")
                        }*/
                        true
                    }
                    R.id.menu_search -> {
                        Log.d("Menu", "search seleccionadas")
                        val tag = "ReportsFragment"
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, SearchFragment(), tag)
                            .addToBackStack(tag)
                            .commit()
                        true
                    }

                    R.id.menu_config -> {
                        Log.d("Menu", "Ajustes seleccionadas")
                        val tag = "ConfigurationFragment"
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, ConfigurationFragment(), tag)
                            .addToBackStack(tag)
                            .commit()
                        true
                    }

                    R.id.menu_logout -> {
                        Log.d("Menu", "Logout seleccionado")
                        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show() // Muestra el menú emergente
        }

        // Configura el listener para el SearchView en la barra superior
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Lógica para manejar la sumisión de texto en el SearchView
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Lógica para manejar el cambio de texto en el SearchView
                return true
            }
        })
    }
}
