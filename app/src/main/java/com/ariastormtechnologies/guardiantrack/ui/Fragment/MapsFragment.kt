package com.ariastormtechnologies.guardiantrack.ui.Fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ariastormtechnologies.guardiantrack.R
import com.ariastormtechnologies.guardiantrack.network.RetrofitClient
import com.ariastormtechnologies.guardiantrack.data.models.SosAlert
import com.ariastormtechnologies.guardiantrack.utils.SOSFlashOverlay
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ncorti.slidetoact.SlideToActView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var btnSOS: ImageView
    private lateinit var btnClose: Button
    private lateinit var sliderConfirm: SlideToActView
    private lateinit var textSOS: TextView
    private lateinit var mapOverlay: View
    private lateinit var btnCenterLocation: ImageButton

    private val sosFlasher = SOSFlashOverlay()

    companion object {
        private const val LOCATION_REQUEST_CODE = 1001
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa vistas con view.findViewById
        btnSOS = view.findViewById(R.id.btnSOS)
        btnClose = view.findViewById(R.id.btnClose)
        sliderConfirm = view.findViewById(R.id.sliderConfirm)
        textSOS = view.findViewById(R.id.sosText)
        mapOverlay = view.findViewById(R.id.mapOverlay)
        btnCenterLocation = view.findViewById(R.id.btnCenterLocation)

        // Inicializa proveedor de localización
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        btnCenterLocation.setOnClickListener {
            // 1. Verificamos si ya tenemos permiso de ubicación
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // 2. Si sí tenemos permiso, pedimos la ubicación y movemos cámara
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16f))
                    }
                }

            } else {
                // 3. Si NO tenemos permiso, pedimos permiso al usuario
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }



        // Inicializa SupportMapFragment y pide el mapa async
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        // Bloquea toque en overlay inicialmente
        mapOverlay.setOnTouchListener { _, _ -> true }
        mapOverlay.visibility = View.VISIBLE

        // Animación botón SOS si es AnimatedVectorDrawable
        val drawable = btnSOS.drawable
        if (drawable is AnimatedVectorDrawable) drawable.start()

        // Estado inicial UI
        enableSOSButton()

        btnClose.setOnClickListener {
            disableSOSButton()
            Toast.makeText(requireContext(), "Alerta SOS cancelada", Toast.LENGTH_SHORT).show()
        }

        btnSOS.setOnClickListener {
            sliderConfirm.visibility = View.VISIBLE
            btnClose.visibility = View.VISIBLE
            btnClose.alpha = 1.0f
        }

        sosFlasher.start(mapOverlay)

        sliderConfirm.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                getCurrentCoordinates { coordinates ->
                    if (coordinates != null) {
                        val latitude = coordinates.latitude
                        val longitude = coordinates.longitude

                        // Aquí puedes enviar la alerta con Retrofit usando lat/lng
                        println("Cordenadas Lat: ${latitude} Long: ${longitude}")
                        sendSosAlert(latitude, longitude)
                    } else {
                        Toast.makeText(requireContext(), "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                    }

                    disableSOSButton()
                    sliderConfirm.resetSlider()
                }
            }
        }

    }

    private fun sendSosAlert(latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sos = SosAlert(
                    userId = "687c6679886044c43b1483ea",
                    lat = latitude,
                    lng = longitude
                )

                Log.d("SOS_ALERT", "Enviando: $sos")

                val response = try {
                    RetrofitClient.apiService.sendSos(sos)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("SOS_ERROR", "Excepción al llamar Retrofit: ${e.message}", e)
                        Toast.makeText(requireContext(), "Error al enviar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("SOS_ALERT", "✅ Alerta enviada correctamente")
                        Toast.makeText(requireContext(), "🚨 Alerta SOS enviada", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("SOS_ERROR", "❌ Error del servidor: $errorBody")
                        Toast.makeText(requireContext(), "Error al enviar alerta", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("SOS_ERROR", "❗ Excepción general: ${e.message}", e)
                    Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        enableUserLocation()

        // Mueve el botón "Mi ubicación" para que no quede detrás de la barra
        googleMap.uiSettings.isMyLocationButtonEnabled = false
    }

    private fun getCurrentCoordinates(onResult: (LatLng?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            onResult(null) // Retornamos null si no hay permiso
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                onResult(latLng)
            } else {
                onResult(null)
            }
        }
    }

    private fun enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16f))
                googleMap.addMarker(MarkerOptions().position(userLatLng).title("Tu ubicación"))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation()
        } else {
            Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para desactivar u ocultar el botón SOS
    fun disableSOSButton() {
        btnSOS.visibility = View.GONE
        textSOS.visibility = View.GONE
        mapOverlay.visibility = View.GONE
        sliderConfirm.visibility = View.GONE
        btnClose.visibility = View.GONE
        sosFlasher.stop(mapOverlay)
    }

    // Método para activar o mostrar el botón SOS
    fun enableSOSButton() {
        btnClose.visibility = View.VISIBLE
        btnClose.alpha = 0.7f
        sliderConfirm.visibility = View.GONE
        btnSOS.visibility = View.VISIBLE
        textSOS.visibility = View.VISIBLE
    }


}
