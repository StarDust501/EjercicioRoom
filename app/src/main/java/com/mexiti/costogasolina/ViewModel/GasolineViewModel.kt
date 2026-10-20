package com.mexiti.costogasolina.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mexiti.costogasolina.model.GasolineTransaction
import com.mexiti.costogasolina.room.GasolineDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*
 ViewModel encargado de la lógica de negocio y de la gestión de estados UI
 para la persistencia y consulta de cargas de gasolina usando Room.
 */
class GasolineViewModel(private val gasolineDao: GasolineDao) : ViewModel() {

    /*
     Estado que expone la lista completa de cargas guardadas en tiempo real.
     Convierte el Flow del DAO en un StateFlow con una estrategia de suscripción reactiva.
     */
    val transaction: StateFlow<List<GasolineTransaction>> = gasolineDao.getAllTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /*
     Agrega un nuevo registro de carga de gasolina a la base de datos de manera asíncrona.
     */
    fun addTransaction(
        price: Double,
        liters: Double,
        tip: Double,
        date: String,
        kilometers: Double
    ) {
        viewModelScope.launch {
            val newTransaction = GasolineTransaction(
                pricePerLiter = price,
                liters = liters,
                tip = tip,
                date = date,
                kilometers = kilometers,
                price = price
            )
            gasolineDao.insertTransaction(newTransaction)
        }
    }

    /*
     Obtiene los detalles de una transacción por su identificador único.
     */
    suspend fun getTransactionById(id: Int): GasolineTransaction? {
        return gasolineDao.getTransactionById(id)
    }

    /*
     Actualiza la información de un registro existente.
     */
    fun updateTransaction(transaction: GasolineTransaction) {
        viewModelScope.launch {
            gasolineDao.updateTransaction(transaction)
        }
    }

    /*
    Elimina un registro guardado pasando la entidad completa.
     */
    fun deleteTransaction(transaction: GasolineTransaction) {
        viewModelScope.launch {
            gasolineDao.deleteTransaction(transaction)
        }
    }

    /*
     Elimina un registro de la base de datos buscando por su ID.
     */
    fun deleteTransactionById(id: Int) {
        viewModelScope.launch {
            gasolineDao.deleteTransactionById(id)
        }
    }
}
