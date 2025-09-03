package com.ariastormtechnologies.guardiantrack.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ariastormtechnologies.guardiantrack.data.models.MissingPerson
import com.ariastormtechnologies.guardiantrack.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val missingList: List<MissingPerson>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val person = missingList[position]

        holder.binding.apply {
            // Aquí haces binding con las vistas del item_notification.xml
            stateLabel.text = "ALERTA"
            timestamp.text = formatDate(person.created_at) // Puedes formatear fecha
            //description.text = "${person.full_name}. ${person.description}"
            description.text = "${person.full_name}. Se encuentra desaparecido."
        }
    }

    override fun getItemCount(): Int = missingList.size

    private fun formatDate(dateString: String): String {
        // Aquí puedes formatear created_at si lo deseas
        return dateString.substring(0, 10) // ejemplo simple
    }
}
