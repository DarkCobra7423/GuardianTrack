package com.ariastormtechnologies.guardiantrack.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariastormtechnologies.guardiantrack.data.repositories.ApiService
import com.ariastormtechnologies.guardiantrack.databinding.FragmentAlertBinding
import com.ariastormtechnologies.guardiantrack.network.RetrofitClient
import com.ariastormtechnologies.guardiantrack.ui.adapters.NotificationAdapter
import com.ariastormtechnologies.guardiantrack.utils.NotificationHelper
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlertFragment : Fragment() {

    private lateinit var binding: FragmentAlertBinding
    private lateinit var apiService: ApiService
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura el RecyclerView
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Usa RetrofitClient centralizado
        apiService = RetrofitClient.apiService

        // Llama a la función para obtener las notificaciones
        fetchNotifications()
    }


    private fun fetchNotifications() {
        lifecycleScope.launch {
            try {
                val response = apiService.getMissingPeople()
                if (response.isSuccessful && response.body() != null) {
                    val missingPeople = response.body()!!

                    // Crear canal de notificaciones
                    NotificationHelper.createNotificationChannel(requireContext())

                    // Mostrar notificaciones en barra del sistema
                    missingPeople.forEach { person ->
                        val title = "🚨 Alerta: ${person.full_name}"
                        val message = "${person.description} (${person.city}, ${person.state})"
                        NotificationHelper.showNotification(
                            requireContext(),
                            title,
                            message,
                            person.folio.hashCode()
                        )
                    }

                    // Mostrar en la UI (RecyclerView)
                    notificationAdapter = NotificationAdapter(missingPeople)
                    binding.notificationRecyclerView.adapter = notificationAdapter
                } else {
                    Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Fallo de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
