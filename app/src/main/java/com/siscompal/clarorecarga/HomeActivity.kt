package com.siscompal.clarorecarga

import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.siscompal.clarorecarga.common.ConexionSocket
import com.siscompal.clarorecarga.common.Constantes
import com.siscompal.clarorecarga.common.SharedPreferenceManager
import com.siscompal.clarorecarga.interfaces.FragmentCom
import com.siscompal.clarorecarga.ui.UltimasRecargas
import com.siscompal.clarorecarga.ui.paquetes.PaquetesActivity
import com.siscompal.clarorecarga.ui.paquetes.exito.RealizarPaquetesExito
import com.siscompal.clarorecarga.ui.paquetes.kalley.RealizarPaquetesKalley
import com.siscompal.clarorecarga.ui.paquetes.wings.RealizarPaquetesWings
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.siscompal.clarorecarga.interfaces.ItemSeleccionado
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HomeActivity : AppCompatActivity(), FragmentCom,ItemSeleccionado {
    var toolbar:Toolbar?=null
    var parametros:String="";
    val version= Constantes.VERSION_CODE;
    lateinit var fechaActual:String
    lateinit var horaActual:String
    var idCliente:String?=null
    var hmac:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //supportActionBar?.hide()
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitle(R.string.tituloToolbar)
        setSupportActionBar(toolbar)

        //Se obtiene la fecha y la hora actual
        fechaActual= SimpleDateFormat("yyyy-MM-dd ").format(Date());
        horaActual= SimpleDateFormat("HH:mm:ss").format(Date());
        idCliente= SharedPreferenceManager.getSomeStringValue("ID")


        //Se envian los datos al metodo que va a generar la Key de tipo Hexadecimal para ser enviada a Distrirecarga
        hmac = calculateRFC2104HMAC(fechaActual + horaActual, "android123*")
        //Parametros que van a hacer enviados en la peticion Socket en el Inicio de Sesion



    //    val appBarConfiguration = AppBarConfiguration.Builder(
      //      R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        //).build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
       NavigationUI.setupWithNavController(navView, navController)


    }
    private fun toHexString(bytes: ByteArray): String? {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }
    //Metodo que genera la Key de tipo Hexadecimal
    fun calculateRFC2104HMAC(data: String, key: String): String? {
        val signingKey = SecretKeySpec(key.toByteArray(), Constantes.HMAC_MD5_ALGORITHM)
        val mac: Mac = Mac.getInstance(Constantes.HMAC_MD5_ALGORITHM)
        mac.init(signingKey)
        return toHexString(mac.doFinal(data.toByteArray()))
    }

    override fun itemSeleccionado(item: String) {
        if(item=="Consultar Saldo"){
            parametros = "mov|sal|" + horaActual.toString() + "|" + hmac + "|" +idCliente+ "|" + version
            Acciones().execute()
        }
        else if(item=="Consultar Ultimas Recargas"){
            var intent=Intent(this, UltimasRecargas::class.java)
            startActivity(intent)
        }
        else if(item=="Mi Código Efecty"){
            var alertDialog=AlertDialog.Builder(this)
            alertDialog.setTitle("Datos Para Consignar en Efecty")
            alertDialog.setMessage("Convenio: 110911\nReferencia: 110${idCliente}")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->  })
            alertDialog.show()
        }
        else if(item=="Bancos y Promociones"){
            var intent=Intent(this,BancosActivity::class.java)
            startActivity(intent)

        }
        else if(item=="Cerrar Sesión"){
            SharedPreferenceManager.deleteSomeValue()
            var intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun paquetesClaro() {

       var intent: Intent= Intent(this,
           PaquetesActivity::class.java)
       intent.putExtra("paquete","claro")
       startActivity(intent)

   }

    override fun paquetesTigo() {
        var intent: Intent= Intent(this,
            PaquetesActivity::class.java)
        intent.putExtra("paquete","tigo")
        startActivity(intent)

    }

    override fun paqueteVirgin() {
        var intent:Intent= Intent(this, PaquetesActivity::class.java)
        intent.putExtra("paquete","virgin")
        startActivity(intent)

    }
    override fun paqueteEtb() {
        var intent:Intent= Intent(this, PaquetesActivity::class.java)
        intent.putExtra("paquete","etb")
        startActivity(intent)

    }

    override fun paqueteAvantel() {
        var intent:Intent= Intent(this, PaquetesActivity::class.java)
        intent.putExtra("paquete","avantel")
        startActivity(intent)

    }

    override fun paqueteMovistar() {
        var intent:Intent= Intent(this, PaquetesActivity::class.java)
        intent.putExtra("paquete","movistar")
        startActivity(intent)

    }

    override fun paqueteExito() {
        var intent:Intent= Intent(this, RealizarPaquetesExito::class.java)
        intent.putExtra("paquete","exito")
        startActivity(intent)

    }

    override fun paqueteKalley() {
        var intent:Intent= Intent(this, RealizarPaquetesKalley::class.java)
        intent.putExtra("paquete","kalley")
        startActivity(intent)

    }

    override fun paqueteWings() {
        var intent:Intent= Intent(this, RealizarPaquetesWings::class.java)
        intent.putExtra("paquete","wings")
        startActivity(intent)

    }

    inner class Acciones:AsyncTask<Void,Void,Boolean>(){

        private lateinit var response:String
        private lateinit var respuesta:String
        lateinit var saldo:String
        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            var alertDialog=AlertDialog.Builder(this@HomeActivity)
            alertDialog.setTitle("Saldo Actual")
            alertDialog.setMessage("Su saldo es: $"+saldo)
                .setPositiveButton("OK",DialogInterface.OnClickListener { dialog, id ->

                })
                alertDialog.show()
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            if(params.isNotEmpty()){

            }
            try {
                response= ConexionSocket().ClSocket(parametros)
                Log.i("INFO", response)
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            if(response.isNotEmpty()){
                var reqJson: JSONObject = JSONObject(response);
                respuesta=reqJson.getString("respuesta")

                if(respuesta.equals("ok")){

                    saldo= reqJson.getString("saldo")
                    publishProgress()
                    return true


                }
            }
            return false
        }

        override fun onCancelled(result: Boolean?) {
            super.onCancelled(result)
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }
    }






}