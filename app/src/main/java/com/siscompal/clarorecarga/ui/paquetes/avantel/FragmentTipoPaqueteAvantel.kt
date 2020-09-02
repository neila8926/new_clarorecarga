package com.siscompal.clarorecarga.ui.paquetes.avantel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView

import com.siscompal.clarorecarga.R

/**
 * A simple [Fragment] subclass.
 */
class FragmentTipoPaqueteAvantel : Fragment() {
    private var todoIAvantel:Button?=null
    private var vozAvantel:Button?=null
    private var internetAva:Button?=null
    private var whatsappAvan:Button?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_tipo_paquete_avantel, container, false)
        todoIAvantel=view.findViewById(R.id.todoIAvantel)
        vozAvantel=view.findViewById(R.id.vozAvantel)
        internetAva=view.findViewById(R.id.internetAva)
        whatsappAvan=view.findViewById(R.id.whatsappAvan)

        todoIAvantel?.setOnClickListener {
            var intent:Intent= Intent(context,RealizarPaquetesAvantel::class.java)
            intent.putExtra("avantel","todoInc")
            startActivity(intent)
        }
        vozAvantel?.setOnClickListener {
            var intent:Intent= Intent(context,RealizarPaquetesAvantel::class.java)
            intent.putExtra("avantel","voz")
            startActivity(intent)
        }
        internetAva?.setOnClickListener {
            var intent:Intent= Intent(context,RealizarPaquetesAvantel::class.java)
            intent.putExtra("avantel","internet")
            startActivity(intent)
        }
        whatsappAvan?.setOnClickListener {
            var intent:Intent= Intent(context,RealizarPaquetesAvantel::class.java)
            intent.putExtra("avantel","whatsapp")
            startActivity(intent)
        }


        return view
    }

}
