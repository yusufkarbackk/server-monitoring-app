package com.example.server_monitoring

data class SensorDataRequest(
    val encryptedSuhu: String,
    val encryptedKelembaban: String,
    val encryptedTeganganAC: String
)

data class SensorDataResponse(
    val decryptedSuhu: Double,
    val decryptedKelembaban: Double,
    val decryptedTeganganAC: Double
)
