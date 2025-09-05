package com.ariastormtechnologies.guardiantrack.ui

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.ariastormtechnologies.guardiantrack.data.models.MissingPerson
import com.ariastormtechnologies.guardiantrack.network.RetrofitClient
import com.ariastormtechnologies.guardiantrack.ui.Fragment.ProfileFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WebFormInterface(private val context: Context) {

    private val apiService = RetrofitClient.apiService

    @JavascriptInterface
    fun submitMissingPerson(jsonData: String) {
        Log.d("WebFormInterface", "submitMissingPerson called with: $jsonData")
        try {
            val gson = Gson()
            val person = gson.fromJson(jsonData, MissingPerson::class.java)

            val userId = ProfileFragment.GLOBAL_USER_ID ?: ""
            val personWithUserId = person.copy(userId = userId)

            Log.d("WebFormInterface", "Person object after adding userId: $personWithUserId")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.createMissingPerson(personWithUserId)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "✅ Datos guardados correctamente", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "❌ Error: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebFormInterface", "❌ Error al enviar datos", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "❌ Error de red", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("WebFormInterface", "❌ Error al parsear JSON", e)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "❌ Error en datos enviados", Toast.LENGTH_LONG).show()
            }
        }
    }
}
