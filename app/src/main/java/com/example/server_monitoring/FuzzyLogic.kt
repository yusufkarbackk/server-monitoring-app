package com.example.server_monitoring

class FuzzyLogic {
    fun lowMembership(value: Double, min: Double, max: Double): Double {
        return if (value < min) 1.0 else if (value > max) 0.0 else 0.0
    }

//    fun normalMembership(value: Double, min: Double, max: Double): Double {
//        return if (value <= min || value >= max) 0.0 else (value - min) / (max - min)
//    }

    fun highMembership(value: Double, min: Double, max: Double): Double {
        return if (value > max) 1.0 else if (value < min) 0.0 else 0.0
    }

    fun fuzzify(sensorData: SensorData): Map<String, Map<String, Double>> {  
        return mapOf(
            "suhu" to mapOf(
                "low" to lowMembership(sensorData.suhu!!, 18.0, 22.0),
                "high" to highMembership(sensorData.suhu, 27.0, 29.0)
            ),
            "kelembaban" to mapOf(
                "low" to lowMembership(sensorData.kelembaban!!, 30.0, 40.0),
                "high" to highMembership(sensorData.kelembaban, 50.0, 60.0)
            ),
            "teganganAC" to mapOf(
                "low" to lowMembership(sensorData.teganganAC!!, 220.0, 220.0),
                "high" to highMembership(sensorData.teganganAC, 220.0, 220.0)
            )
        )
    }

    fun getAboveThresholdSensors(fuzzifiedData: Map<String, Map<String, Double>>): List<String> {
        val sensors = mutableListOf<String>()
        if ((fuzzifiedData["suhu"]?.get("high") ?: 0.0) > 0.5) sensors.add("suhu")
        if ((fuzzifiedData["kelembaban"]?.get("high") ?: 0.0) > 0.5) sensors.add("kelembaban")
        if ((fuzzifiedData["teganganAC"]?.get("high") ?: 0.0) > 0.5) sensors.add("teganganAC")
        return sensors
    }

    fun getBelowThresholdSensors(fuzzifiedData: Map<String, Map<String, Double>>): List<String> {
        val sensors = mutableListOf<String>()
        if ((fuzzifiedData["suhu"]?.get("low") ?: 0.0) > 0.5) sensors.add("suhu")
        if ((fuzzifiedData["kelembaban"]?.get("low") ?: 0.0) > 0.5) sensors.add("kelembaban")
        if ((fuzzifiedData["teganganAC"]?.get("low") ?: 0.0) > 0.5) sensors.add("teganganAC")
        return sensors
    }
    fun getSensorValue(sensor: String, sensorData: SensorData): Double {
        return when (sensor) {
            "suhu" -> sensorData.suhu!!
            "kelembaban" -> sensorData.kelembaban!!
            "teganganAC" -> sensorData.teganganAC!!
            else -> 0.0
        } }
}
