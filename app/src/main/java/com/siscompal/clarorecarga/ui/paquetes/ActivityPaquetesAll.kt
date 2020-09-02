package com.siscompal.clarorecarga.ui.paquetes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siscompal.clarorecarga.R
import com.siscompal.clarorecarga.interfaces.ClickListenerUltiRec
import com.siscompal.clarorecarga.interfaces.FragmentCom
import com.siscompal.clarorecarga.local.modelo.TipPaquetes
import com.siscompal.clarorecarga.ui.pines.RealizarPines
import java.lang.ClassCastException

class ActivityPaquetesAll:Fragment() {

    var lista:RecyclerView?=null
    var adaptador:AdaptadorPaquetes?=null
    //administradir del diseño
    var layoutManager:RecyclerView.LayoutManager?=null
    var listenerTipoPaq:FragmentCom?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista=inflater.inflate(R.layout.paquetes_all, container, false)
        var paquetes=ArrayList<TipPaquetes>()
        paquetes.add(TipPaquetes("Paquetes Claro",R.drawable.paquete_claro))
        paquetes.add(TipPaquetes("Paquetes Movistar",R.drawable.recarga_movistar))
        paquetes.add(TipPaquetes("Paquetes Tigo",R.drawable.paquete_tigo))
        paquetes.add(TipPaquetes("Paquetes Avantel",R.drawable.recarga_avantel))
        paquetes.add(TipPaquetes("Paquetes Virgin",R.drawable.paquete_virgin))
        paquetes.add(TipPaquetes("Paquetes Etb",R.drawable.paquete_etb_n))
        paquetes.add(TipPaquetes("Paquetes Exito",R.drawable.recarga_exito))
        paquetes.add(TipPaquetes("Paquetes Kalley",R.drawable.paquete_kalley))
        paquetes.add(TipPaquetes("Paquetes Wings",R.drawable.paquete_wings))
        paquetes.add(TipPaquetes("Pines Netflix",R.drawable.pin_netflix))
        paquetes.add(TipPaquetes("Pines Spotify",R.drawable.spotify))
        paquetes.add(TipPaquetes("Pines Imvu",R.drawable.imvu))
        paquetes.add(TipPaquetes("Pines Minecraft",R.drawable.minecraft))

        lista=vista?.findViewById(R.id.listaPaquetesA)
        lista?.setHasFixedSize(true)
        var intentPines=Intent(context,RealizarPines::class.java)

        layoutManager=LinearLayoutManager(context)
        lista?.layoutManager=layoutManager

        adaptador=AdaptadorPaquetes(paquetes, object:ClickListenerUltiRec{
            override fun onClick(vista: View, index: Int) {

                if(paquetes.get(index).nombre=="Paquetes Claro") listenerTipoPaq?.paquetesClaro()
                if(paquetes.get(index).nombre=="Paquetes Movistar") listenerTipoPaq?.paqueteMovistar()
                if(paquetes.get(index).nombre=="Paquetes Tigo") listenerTipoPaq?.paquetesTigo()
                if(paquetes.get(index).nombre=="Paquetes Avantel") listenerTipoPaq?.paqueteAvantel()
                if(paquetes.get(index).nombre=="Paquetes Virgin") listenerTipoPaq?.paqueteVirgin()
                if(paquetes.get(index).nombre=="Paquetes Etb") listenerTipoPaq?.paqueteEtb()
                if(paquetes.get(index).nombre=="Paquetes Exito") listenerTipoPaq?.paqueteExito()
                if(paquetes.get(index).nombre=="Paquetes Kalley") listenerTipoPaq?.paqueteKalley()
                if(paquetes.get(index).nombre=="Paquetes Wings") listenerTipoPaq?.paqueteWings()
                if(paquetes.get(index).nombre=="Pines Netflix") {
                    intentPines.putExtra("pin","netflix")
                    startActivity(intentPines)
                }
                if(paquetes.get(index).nombre=="Pines Spotify") {
                    intentPines.putExtra("pin","spotify")
                    startActivity(intentPines)
                }
                if(paquetes.get(index).nombre=="Pines Imvu"){
                    intentPines.putExtra("pin","imvu")
                    startActivity(intentPines)
                }
                if(paquetes.get(index).nombre=="Pines Minecraft"){
                    intentPines.putExtra("pin","minecraft")
                    startActivity(intentPines)
                }
            }

        })
        lista?.adapter=adaptador



        return vista
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listenerTipoPaq=context as FragmentCom
        }catch (e: ClassCastException)
        {throw ClassCastException(context.toString()+"Debes implemetar la Interfaz")
        }
    }
}