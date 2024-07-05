package com.example.server_monitoring

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("decrypt-data")
    suspend fun sendSensorData(@Body sensorDataRequest: SensorDataRequest): SensorDataResponse
}