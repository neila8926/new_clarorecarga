package com.siscompal.clarorecarga.ui.paquetes.kalley

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

/**
 * A simple [Fragment] subclass.
 */
class ProductoFragmentKalley : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductoRecyclerViewAdapter
    private var productos: List<Producto> = ArrayList()
    private var listener:DetallesPaquete?=null

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_producto_list, container, false)

        //Obtenemos el ViewModel
        productViewModel= ViewModelProvider(this).get(ProductViewModel::class.java)


        //cuando se instanciara el adapter
        productAdapter= ProductoRecyclerViewAdapter()

        productAdapter.setOnclicListener(View.OnClickListener {
            var producto= it.tag as Producto

            var nombre=producto.nombre
            var valor = producto.valor
            var descripcion=producto.observacion
            var id=producto.id

            listener?.obtenerDatosPaquetes(nombre,valor,descripcion,id)


        })

        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = productAdapter
            }
        }
        //Observer de los productos
        productViewModel.getProductKalley().observe(viewLifecycleOwner, Observer {
            productos= it
            Log.i("INFO", " Prueba "+productos)
            productAdapter.setData(productos)
        })
        return view
    }


    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ProductoFragmentKalley().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener=context as DetallesPaquete
        }catch (e: ClassCastException){
            throw ClassCastException(context.toString()+"Debes Implementar la Interfaz")
        }
    }

}
