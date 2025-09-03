package com.ariastormtechnologies.guardiantrack.data.models

data class MissingPerson(
    val userId: String,
    val folio: String,
    val full_name: String,
    val curp: String,
    val age: Int,
    val gender: String,
    val state: String,
    val city: String,
    val missing_date: String,
    val protocol: String,
    val status: String,
    val condition: String?,
    val photo_urls: List<String>,
    val description: String,
    val complexion: String,
    val reporter: String,
    val notes: String,
    val created_at: String
)
