package com.siscompal.clarorecarga.ui.paquetes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siscompal.clarorecarga.R
import com.siscompal.clarorecarga.interfaces.ClickListenerUltiRec
import com.siscompal.clarorecarga.local.modelo.TipPaquetes
import kotlinx.android.synthetic.main.item_paquete.view.*

class AdaptadorPaquetes(item:ArrayList<TipPaquetes>, var  listener:ClickListenerUltiRec):RecyclerView.Adapter<AdaptadorPaquetes.ViewHolder>() {
    var items:ArrayList<TipPaquetes>? =  null
    init {
        this.items=item
    }

    //Vamos a crear la vista y asigmar el elemento que queremos renderizar
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdaptadorPaquetes.ViewHolder {
       val vista=LayoutInflater.from(parent.context).inflate(R.layout.item_paquete,parent,false)
        val viewHolder=ViewHolder(vista,listener)
        return viewHolder

    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    override fun onBindViewHolder(holder: AdaptadorPaquetes.ViewHolder, position: Int) {
        //obtenemos el elemento actual
        val item=items?.get(position)
        //mapear los valores que tengo el el Holder
        holder.nombre?.setText(item?.nombre!!)
        holder.logo?.setImageResource(item?.logo!!)


    }
    class ViewHolder(vista:View,listener:ClickListenerUltiRec):RecyclerView.ViewHolder(vista),View.OnClickListener{
        var vista=vista
        var logo:ImageView?=null
        var nombre:TextView?=null
        var listener:ClickListenerUltiRec? = null

        init{
            logo=vista.findViewById(R.id.itemLogoP)
            nombre=vista.findViewById(R.id.itemNombreP)
            this.listener=listener
            vista.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            this.listener?.onClick(v!!,adapterPosition)
        }

    }
}