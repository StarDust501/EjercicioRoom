package com.mexiti.costogasolina.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mexiti.costogasolina.model.GasolineTransaction

/**
 * Base de datos principal de Room de la aplicación.
 * Define la entidad [GasolineTransaction] y ofrece el método de acceso al DAO.
 */
@Database(entities = [GasolineTransaction::class], version = 1, exportSchema = false)
abstract class GasolineDatabase : RoomDatabase() {

    /*
    Retorna la interfaz DAO para manipular las transacciones de gasolina.
     */
    abstract fun gasolineDao(): GasolineDao

    companion object {
        @Volatile
        private var INSTANCE: GasolineDatabase? = null

        /*
         Retorna la instancia Singleton de la base de datos de Room.
         Garantiza una única instancia abierta en memoria mediante sincronización.
         */
        fun getDatabase(context: Context): GasolineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GasolineDatabase::class.java,
                    "gasoline_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
