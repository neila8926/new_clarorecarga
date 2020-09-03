package com.siscompal.clarorecarga

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.get
import com.siscompal.clarorecarga.interfaces.ItemSeleccionado
import com.siscompal.clarorecarga.local.modelo.Info
import com.siscompal.clarorecarga.ui.UltimasRecargas


class InfoClienteFragment : Fragment() {
    var listaInfo:ListView? = null
    var itemSeleccionado: ItemSeleccionado?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista= inflater.inflate(R.layout.fragment_info_cliente, container, false)
        
        var info:ArrayList<Info> = ArrayList()
        info.add(Info("Consultar Saldo",R.drawable.ic_baseline_monetization_on_24))
        info.add(Info("Consultar Ultimas Recargas",R.drawable.ic_baseline_battery_charging_full_24))
        info.add(Info("Mi Código Efecty",R.drawable.ic_efecty_list_alt_24))
        info.add(Info("Bancos y Promociones",R.drawable.ic_bancos_local_atm_24))
        info.add(Info("Cerrar Sesión",R.drawable.ic_baseline_exit_to_app_24))

        listaInfo=vista.findViewById(R.id.listaInfo)
       // val adaptador=ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1,info)
        var adaptador=adaptadorCustomInfo(context!!,info)
        listaInfo?.adapter=adaptador

        listaInfo?.onItemClickListener=AdapterView.OnItemClickListener { adapterView, view, i, l ->

            itemSeleccionado?.itemSeleccionado(info.get(i).nombre)
        }
        return vista
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        itemSeleccionado=context as ItemSeleccionado
    }


}