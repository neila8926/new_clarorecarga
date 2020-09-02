package com.siscompal.clarorecarga.ui.recargas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.siscompal.clarorecarga.local.ProductRepository
import com.siscompal.clarorecarga.local.modelo.Producto

class RecargasViewModel(aplication: Application):AndroidViewModel(aplication) {
    private var productoRepository:ProductRepository
    private var recargas:LiveData<List<Producto>>


    init {
        productoRepository=ProductRepository(aplication)
        recargas=productoRepository. getRecargas()
    }
    fun recargas():LiveData<List<Producto>>{
        return recargas
    }
}