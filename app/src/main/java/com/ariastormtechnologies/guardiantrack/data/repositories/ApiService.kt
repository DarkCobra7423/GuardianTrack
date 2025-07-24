package com.ariastormtechnologies.guardiantrack.data.repositories

import com.ariastormtechnologies.guardiantrack.data.models.SosAlert
import com.ariastormtechnologies.guardiantrack.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>

    @POST("sos")
    suspend fun sendSos(@Body sosAlert: SosAlert): Response<SosAlert>
}
