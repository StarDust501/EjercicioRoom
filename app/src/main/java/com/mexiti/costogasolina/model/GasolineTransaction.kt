package com.mexiti.costogasolina.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gasoline_transactions")
data class GasolineTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val price: Double,
    val liters: Double,
    val tip: Double,
    val date: String,
    val kilometers: Double,
    val pricePerLiter: Double
)
