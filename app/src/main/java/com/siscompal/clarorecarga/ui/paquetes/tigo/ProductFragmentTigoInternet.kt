package com.siscompal.clarorecarga.ui.paquetes.tigo

import android.content.Context
import android.os.Bundle
import android.util.Log
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

class ProductFragmentTigoInternet: Fragment() {
    private  lateinit var productAdapter: ProductoRecyclerViewAdapter
    private lateinit var productView: ProductViewModel
    private var productos:List<Producto> = ArrayList()
    var listener: DetallesPaquete?= null
    private var columnCount = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_producto_list,container,false)


        //obtenemos el ViewModel
        productView=ViewModelProvider(this).get(ProductViewModel::class.java)
        //se crea una instancia del adaptador
        productAdapter= ProductoRecyclerViewAdapter()

        productAdapter.setOnclicListener(View.OnClickListener {
            var producto= it.tag as Producto
            var nombre=producto.nombre
            var valor=producto.valor
            var descripcion=producto.observacion
            var id=producto.id

            listener?.obtenerDatosPaquetes(nombre!!,valor!!,descripcion!!,id!!)
        })

            if(view is RecyclerView){
                with(view){
                    layoutManager=when{
                        columnCount<=1->LinearLayoutManager(context)
                        else->GridLayoutManager(context,columnCount)

                    }
                    adapter=productAdapter
                }
            }
            //observador del ViewModel
        productView.getProductsInternet().observe( viewLifecycleOwner, Observer {
            productos=it
            Log.i("INFOR"," paq "+productos)
            productAdapter.setData(productos)

        })



        return view

    }
    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCont: Int) = ProductFragmentTigoInternet.apply {
            var args=Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCont)
            }
        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener=context as DetallesPaquete

        }catch (e: ClassCastException){
          throw ClassCastException(context.toString()+"Debes Implementar la Interfaz")
        }

    }

}