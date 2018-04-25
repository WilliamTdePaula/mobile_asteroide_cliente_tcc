package com.example.a16254868.usuarioasteroide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferencias = getSharedPreferences("Cliente", Context.MODE_PRIVATE)

        botaoLogar.setOnClickListener {

            doAsync {

                val login = txtLoginUsuario.text.toString()
                val senha = txtSenhaUsuario.text.toString()

                val url = "http://10.107.144.9:3000/api/v1/autenticar/cliente"

                val map = HashMap<String, String>()
                map.put("login", login)
                map.put("senha", senha)

                val resultado = HttpConnection.post(url, map)

                Log.d("API", resultado)

                uiThread {

                    val jsonarray = JSONArray(resultado)

                    //Verificando se a senha ou usuário estão corretas
                    if(jsonarray.isNull(0)){
                        Toast.makeText(applicationContext, "Usuário ou senha incorreto", Toast.LENGTH_SHORT).show()
                    }else{
                        for (i in 0 until jsonarray.length()) {

                            preferencias.edit().putString("login", login).apply()
                            preferencias.edit().putString("senha", senha).apply()

                            val intent = Intent(applicationContext, HomeUsuarioActivity::class.java)

                            startActivity(intent)

                            finish()
                        }
                    }
                }

            }

        }

        botaoCadastrar.setOnClickListener {

            val intent = Intent(this, CadastroUsuarioActivity::class.java)

            intent.putExtra("tela", "Cadastro")

            startActivity(intent)

        }
    }
}
