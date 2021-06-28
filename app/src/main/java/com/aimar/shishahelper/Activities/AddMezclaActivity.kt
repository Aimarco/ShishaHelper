package com.aimar.shishahelper.Activities

import Models.MarcaTabaco
import Models.MezclaTabaco
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aimar.shishahelper.R
import com.aimar.shishahelper.RecyclerView.SpinnerAdapter
import com.aimar.shishahelper.RecyclerView.SpinnerAdapterSabor
import com.google.firebase.firestore.FirebaseFirestore


class AddMezclaActivity  : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    lateinit var listaTabacos:MutableList<MarcaTabaco>
    lateinit var btnEnviar : Button
    lateinit var edtSabor1 : EditText
    lateinit var edtSabor2 : EditText
    lateinit var edtPorcentaje1 : EditText
    lateinit var edtPorcentaje2 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevamezcla)
        val spinnerTabaco1 = findViewById<Spinner>(R.id.spinnertabaco1)
        val spinnerTabaco2 = findViewById<Spinner>(R.id.spinnertabaco2)
        val spinnerSabor = findViewById<Spinner>(R.id.spinnerSabor)

        val  intent: Intent = getIntent();
        listaTabacos = mutableListOf()
        val listaSabores = arrayListOf<String>()
        val mezcla:MezclaTabaco = MezclaTabaco()
        //cargar tabacos
        db.collection("Tabacos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val tabaco:MarcaTabaco = MarcaTabaco()
                    Log.d("TABACO", "${document.id} => ${document.data}")
                    tabaco.id =document.id
                    tabaco.nombre = document.data["nombre"].toString()
                    tabaco.imagen = document.data["imagen"].toString()
                    listaTabacos.add(tabaco)
                }
                val customDropDownAdapter = SpinnerAdapter(this, listaTabacos)
                spinnerTabaco1.adapter = customDropDownAdapter
                spinnerTabaco2.adapter = customDropDownAdapter
                listaSabores.add("Citrico")
                listaSabores.add("Dulce")
                listaSabores.add("Afrutado")
                val customDropDownAdapterSabor = SpinnerAdapterSabor(this,listaSabores)
                spinnerSabor.adapter = customDropDownAdapterSabor

            }
            .addOnFailureListener { exception ->
                Log.d("ERROR", "Error getting documents: ", exception)
            }

        //fin carga tabacos


        spinnerTabaco1?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mezcla.tabaco1 = listaTabacos[position].nombre
            }
        }

        spinnerTabaco2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mezcla.tabaco2 = listaTabacos[position].nombre
            }
        }

        spinnerSabor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mezcla.sabor = listaSabores[position]
            }
        }

        btnEnviar = findViewById(R.id.btnNuevoSabor)
        edtSabor1 = findViewById(R.id.edtSabor1)
        edtSabor2 = findViewById(R.id.edtSabor2)
        edtPorcentaje1 = findViewById(R.id.edtPorcentaje1)
        edtPorcentaje2 = findViewById(R.id.edtPorcentaje2)


        btnEnviar.setOnClickListener() {
            if (mezcla.tabaco1.isEmpty()|| mezcla.tabaco2.isEmpty() || edtSabor1.text.isEmpty() || edtSabor2.text.isEmpty()
                || edtPorcentaje1.text.isEmpty() || edtPorcentaje2.text.isEmpty() || mezcla.sabor.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                db.collection("Mezclas").document().set(
                    hashMapOf("tabaco1" to mezcla.tabaco1,
                        "tabaco2" to mezcla.tabaco2,
                        "sabor1" to edtSabor1.text.toString(),
                        "sabor2" to edtSabor2.text.toString(),
                        "porcentaje1" to Integer.parseInt(edtPorcentaje1.text.toString()),
                        "porcentaje2" to Integer.parseInt(edtPorcentaje2.text.toString()),
                        "sabor" to mezcla.sabor)
                )
                Toast.makeText(this, "Mezcla añadida con éxito! Gracias!", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()

            }
        }
    }

}