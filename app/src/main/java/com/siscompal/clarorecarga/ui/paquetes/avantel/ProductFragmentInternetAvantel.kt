package com.siscompal.clarorecarga.ui.paquetes.avantel

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siscompal.clarorecarga.R
import com.siscompal.clarorecarga.interfaces.DetallesPaquete
import com.siscompal.clarorecarga.local.modelo.Producto
import com.siscompal.clarorecarga.ui.paquetes.products.ProductViewModel
import com.siscompal.clarorecarga.ui.ProductoRecyclerViewAdapter
import java.lang.ClassCastException

class ProductFragmentInternetAvantel:Fragment() {
    private lateinit var productAdapater: ProductoRecyclerViewAdapter
    private lateinit var productViewModel: ProductViewModel
    private var listener: DetallesPaquete?=null
    private var productos:List<Producto> = ArrayList()
    var columnCount=1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_producto_list,container,false)
        //se obtiene el ViewModel
        productViewModel=ViewModelProvider(this).get(ProductViewModel::class.java)
        //se instancia el adaptador
        productAdapater= ProductoRecyclerViewAdapter()

        productAdapater.setOnclicListener(View.OnClickListener {
            var producto = it.tag as Producto
            var nombre=producto.nombre
            var precio=producto.valor
            var descripcion=producto.observacion
            var id=producto.id


            listener?.obtenerDatosPaquetes(nombre!!,precio!!,descripcion,id!!)

        })
        if(view is RecyclerView){
            with(view){
                layoutManager=when{
                    columnCount<=1 -> LinearLayoutManager(context)
                    else->GridLayoutManager(context,columnCount)
                }
                adapter=productAdapater
            }

            //Observado
            productViewModel.getProductIntAvantel().observe(viewLifecycleOwner, Observer {
                productos=it
                productAdapater.setData(productos)
            })
        }

        return view
    }
    companion object{
        const val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCount:Int)=ProductFragmentInternetAvantel().apply {
            val args = Bundle().apply {
                putInt(ARG_COLUMN_COUNT,columnCount)

            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener=context as DetallesPaquete
        }catch (e:ClassCastException){
            throw ClassCastException(context.toString()+" Debes Implementar la Interfaz")
        }
    }
}