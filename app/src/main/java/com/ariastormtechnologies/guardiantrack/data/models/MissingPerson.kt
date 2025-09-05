package com.ariastormtechnologies.guardiantrack.data.models

data class MissingPerson(
    val userId: String?,            // Se añade desde Android
    val folio: String?,             // ← Opcional
    val full_name: String?,         // ← HTML lo manda
    val curp: String?,              // ← HTML lo manda
    val age: Int?,                  // ← HTML lo manda
    val gender: String?,            // ← HTML lo manda
    val state: String?,             // ← HTML lo manda
    val city: String?,              // ← HTML lo manda
    val missing_date: String?,      // ← No enviado
    val protocol: String?,          // ← No enviado
    val status: String?,            // ← No enviado
    val condition: String?,         // ← No enviado
    val photo_urls: List<String>?,  // ← HTML lo manda
    val description: String?,       // ← HTML lo manda
    val complexion: String?,        // ← HTML lo manda
    val reporter: String?,          // ← No enviado
    val notes: String?,             // ← No enviado
    val created_at: String?         // ← Backend lo genera
)
