package com.ariastormtechnologies.guardiantrack.data.repositories

import com.ariastormtechnologies.guardiantrack.data.models.MissingPerson
import com.ariastormtechnologies.guardiantrack.data.models.SosAlert
import com.ariastormtechnologies.guardiantrack.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>

    @POST("sos")
    suspend fun sendSos(@Body sosAlert: SosAlert): Response<SosAlert>

    @GET("missing")
    suspend fun getMissingPeople(): Response<List<MissingPerson>>

    @POST("missing")
    suspend fun createMissingPerson(@Body person: MissingPerson): Response<MissingPerson>

}
