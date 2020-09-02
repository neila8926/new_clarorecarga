package com.siscompal.clarorecarga.local

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.siscompal.clarorecarga.local.dao.RecargaDao
import com.siscompal.clarorecarga.local.modelo.Recargas

class RecargaRepository(application: Application) {
    private val recargaDao: RecargaDao?=DistriRoomBD.getDatabase(application)?.recargaDao()

    fun insertRecargas(recarga:Recargas){
        if(recargaDao!=null) InsertAsyncTask(recargaDao).execute(recarga)
    }
    fun getRecargas():LiveData<List<Recargas>>{
        return recargaDao?.getAllRecargas()?: MutableLiveData<List<Recargas>>()

    }
    fun deleteRecargas(){
        recargaDao?.deleteRecargas()
    }


    private class InsertAsyncTask(private val recargaDao: RecargaDao): AsyncTask<Recargas, Void, Void>() {
        override fun doInBackground(vararg recargas: Recargas?): Void? {
            for(recarga in recargas ){
                if (recarga != null){
                    recargaDao.insertRecargas(recarga)
                }

            }

            return null
        }

    }

}