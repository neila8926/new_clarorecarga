package com.siscompal.clarorecarga

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.siscompal.clarorecarga.common.ConexionSocket
import com.siscompal.clarorecarga.common.Constantes
import com.siscompal.clarorecarga.common.SharedPreferenceManager
import com.siscompal.clarorecarga.local.ProductRepository
import com.siscompal.clarorecarga.local.RecargaRepository
import com.siscompal.clarorecarga.local.modelo.Producto
import com.siscompal.clarorecarga.ui.paquetes.products.ProductViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {

    var parametros:String="";
    val version=Constantes.VERSION_CODE;
    val ESTADO_BUTTON="SESION"
    private lateinit var progressBarInicio:ProgressDialog
    private lateinit var edtUsuario:TextInputLayout
    private lateinit var edtPassword:TextInputLayout
    private lateinit var btnIngresar:Button
    private lateinit var idRegistrase:TextView
    private lateinit var idSesionI:RadioButton
    var btnDesactivado=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        if(SharedPreferenceManager.getSomeBooleanValue(ESTADO_BUTTON)==true){
            var intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }
        edtUsuario=findViewById(R.id.tUser);
        edtPassword=findViewById(R.id.tPass);
        btnIngresar=findViewById(R.id.idIngresar);
        idRegistrase=findViewById(R.id.idRegistrase)
        idSesionI=findViewById(R.id.idSesionI)


        btnDesactivado=idSesionI.isChecked

        idSesionI.setOnClickListener {
            if(btnDesactivado)
                idSesionI.isChecked=false
            btnDesactivado=idSesionI.isChecked
        }

        idRegistrase.setOnClickListener {

            var intent =Intent(this,RegistroActivity::class.java)
            startActivity(intent)
        }

        btnIngresar.setOnClickListener{view->

            if(edtUsuario.editText?.text?.isEmpty()==true){
                edtUsuario.setError("Digite Usuario")
            }
            if(edtPassword.editText?.text?.isEmpty()==true){
                edtPassword.setError("Digite Contraseña")

            }else {
                if(edtUsuario.editText?.text?.isNotEmpty()==true && edtPassword?.editText?.text?.isNotEmpty()==true) {

                    var user=edtUsuario.editText?.text.toString().trim().replace("\\s","")

                    //Se obtiene la fecha y la hora actual
                    val fechaActual=SimpleDateFormat("yyyy-MM-dd ").format(Date());
                    val horaActual=SimpleDateFormat("HH:mm:ss").format(Date());

                    //Se envian los datos al metodo que va a generar la Key de tipo Hexadecimal para ser enviada a Distrirecarga
                    val hmac = calculateRFC2104HMAC(fechaActual + horaActual, "android123*")
                    //Parametros que van a hacer enviados en la peticion Socket en el Inicio de Sesion
                    parametros = "mov|log|" + horaActual.toString() + "|" + hmac + "|" +user+ "|" + edtPassword.editText?.getText().toString().toString() + "|" + version


                        Ingresar().execute()

                }
                else {
                    Toast.makeText(this,"Digite usuario y contraseña",Toast.LENGTH_SHORT).show()
                }

            }}

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

    inner class Ingresar: AsyncTask<Void,Int,Boolean>(){
        private lateinit var productViewModel: ProductViewModel
        private var productRepository:ProductRepository=ProductRepository(application)
        private var recargasRepository:RecargaRepository= RecargaRepository(application)
        private lateinit var response:String
        private  var respuesta:String="Error de Conexión"

        override fun onPreExecute() {
            super.onPreExecute()

            progressBarInicio= ProgressDialog(this@MainActivity)
            progressBarInicio.setTitle("Iniciando Sesión")
            progressBarInicio.setMessage("Procesando...")
            progressBarInicio.setCancelable(false)
            progressBarInicio.show()
        }


        override fun doInBackground(vararg params: Void?): Boolean? {
            if(params.isNotEmpty()){

            }
            try {
                response = ConexionSocket().ClSocket(parametros)
                Log.i("req", "Respuesta " + response)
                if(response.equals("Error de Conexión")==false){
                    productRepository.deleteProduct()
                    recargasRepository.deleteRecargas()
                    response = response.replace("\\n", "");

                    var reqJson:JSONObject= JSONObject(response);
                    respuesta=reqJson.getString("respuesta")
                    if(respuesta.equals("Login exitoso")) {

                        val idCliente:String=reqJson.getString("id")
                        //Almacenamos en las preferencias el Id del Cliente
                        SharedPreferenceManager.setSomeStringValue("ID",idCliente)
                        Log.i("req", "probando el nuevo JSON $idCliente")
                        val producto: JSONArray = reqJson.getJSONArray("pr")
                        var size=producto.length()
                        var x=size*100

                        for(i in 0  ..  size){
                            val data: JSONObject = producto.getJSONObject(i)
                            var producto= Producto(data.getString("id").toInt(), data.getString("pr_n"),data.getString("val").toInt(),data.getString("obs"),data.getString("op_i").toInt(),data.getString("op_n"))
                            Log.i("INFO", " "+producto.toString());
                            productRepository.insertProductos(producto)
                            publishProgress(x*size)
                            //productReposit.insertProductos(producto)
                          }
                       }
                }
                return false

            }catch ( ex: Exception) {
                ex.printStackTrace()
            }
            return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            progressBarInicio.progress= values[0]!!

        }


        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)

            progressBarInicio.dismiss()

            if(result==true) {
                SharedPreferenceManager.setSomeBooleanValue(ESTADO_BUTTON,idSesionI.isChecked)
                val intent : Intent=Intent(this@MainActivity,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this@MainActivity,respuesta,Toast.LENGTH_LONG).show()
            }

        }

        override fun onCancelled() {
            super.onCancelled()
        }
    }
}

