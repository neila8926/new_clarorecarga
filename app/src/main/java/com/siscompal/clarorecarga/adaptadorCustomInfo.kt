package com.siscompal.clarorecarga

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.siscompal.clarorecarga.local.modelo.Info

class adaptadorCustomInfo(var contetex:Context,item:ArrayList<Info>):BaseAdapter() {

    var item:ArrayList<Info>? = null
    init {
        this.item=item
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var holder: ViewHolder? =null
        var vista:View?=p1

        if(vista==null){
            vista=LayoutInflater.from(contetex).inflate(R.layout.item_info,null)
            holder=ViewHolder(vista)
            vista.tag=holder
        }
        else{
            holder=vista.tag as? ViewHolder
        }
        val ite=getItem(p0) as Info
        holder?.nombre?.text=ite.nombre
        holder?.imagen?.setImageResource(ite.logoInfo)

        return vista!!

    }

    override fun getItem(p0: Int): Any {
        return item?.get(p0)!!
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return item?.count()!!
    }
    private class ViewHolder(vista:View){
        var nombre:TextView? =null
        var imagen: ImageView? = null

        init {
            nombre=vista.findViewById(R.id.infoItem)
            imagen=vista.findViewById(R.id.logoInfo)
        }
    }
}