package com.mexiti.costogasolina.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mexiti.costogasolina.model.GasolineTransaction
import kotlinx.coroutines.flow.Flow

/*
Objeto de Acceso a Datos (DAO) para la entidad [GasolineTransaction].
Proporciona las operaciones CRUD para la base de datos Room.
 */
@Dao
interface GasolineDao {

    /**
     * Inserta un nuevo registro de transacción de gasolina en la base de datos.
     * Si ya existe un registro con el mismo ID, se reemplazará.
     *
     * @param transaction Objeto de tipo [GasolineTransaction] a guardar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: GasolineTransaction)

    /*
     Obtiene el flujo continuo de la lista completa de registros guardados,
     ordenados de forma descendente según el identificador.
     */
    @Query("SELECT * FROM gasoline_transactions ORDER BY id DESC")
    fun getAllTransactions(): Flow<List<GasolineTransaction>>

    /*
     Busca y recupera una transacción específica mediante su identificador único.
     */
    @Query("SELECT * FROM gasoline_transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): GasolineTransaction?

    /*
    Actualiza la información de un registro existente en la base de datos.
     */
    @Update
    suspend fun updateTransaction(transaction: GasolineTransaction)

    /*
     Elimina un registro de la base de datos pasando la entidad completa.
     */
    @Delete
    suspend fun deleteTransaction(transaction: GasolineTransaction)

    /*
     Elimina un registro de la base de datos buscando por su identificador único.
     */
    @Query("DELETE FROM gasoline_transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)
}
