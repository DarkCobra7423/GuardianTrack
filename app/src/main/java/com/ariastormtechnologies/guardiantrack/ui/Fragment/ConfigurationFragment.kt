package com.ariastormtechnologies.guardiantrack.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ariastormtechnologies.guardiantrack.R

class ConfigurationFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_configuration, container, false)

        val btnUpdateProfile = view.findViewById<View>(R.id.btnUpdateInformation)
        btnUpdateProfile.setOnClickListener {
            // Aquí puedes abrir un nuevo fragmento, una actividad o un diálogo
            // Ejemplo: abrir un fragmento para editar el perfil
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

}