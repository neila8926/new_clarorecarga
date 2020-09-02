package com.siscompal.clarorecarga.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.siscompal.clarorecarga.local.dao.ProductoDao
import com.siscompal.clarorecarga.local.dao.RecargaDao
import com.siscompal.clarorecarga.local.modelo.Producto
import com.siscompal.clarorecarga.local.modelo.Recargas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Producto::class,Recargas::class),version = 1, exportSchema = false)
abstract class DistriRoomBD: RoomDatabase() {
    abstract fun productoDao():ProductoDao
    abstract fun recargaDao():RecargaDao



    companion object{
        @Volatile
        private var INSTANCE: DistriRoomBD? = null

        fun getDatabase(context: Context):DistriRoomBD{
            val temInstance = INSTANCE
            if(temInstance!=null){
                return temInstance
            }
            synchronized(this){
                val instance =Room.databaseBuilder(
                    context.applicationContext,
                    DistriRoomBD::class.java,
                    "distri_database"
                )  .build()
                INSTANCE=instance
                return instance
            }

        }
    }

}