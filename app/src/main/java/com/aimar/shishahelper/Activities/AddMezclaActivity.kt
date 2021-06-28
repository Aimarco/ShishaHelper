package com.aimar.shishahelper.Activities

import Models.MarcaTabaco
import Models.MezclaTabaco
import Models.SaboresTabaco
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aimar.shishahelper.R
import com.aimar.shishahelper.RecyclerView.SpinnerAdapter
import com.aimar.shishahelper.RecyclerView.SpinnerAdapterSabor
import com.aimar.shishahelper.RecyclerView.SpinnerAdapterSaborMezclas
import com.google.firebase.firestore.FirebaseFirestore


class AddMezclaActivity  : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    lateinit var listaTabacos:MutableList<MarcaTabaco>
    lateinit var btnEnviar : Button
    lateinit var edtPorcentaje1 : EditText
    lateinit var edtPorcentaje2 : EditText
    lateinit var listaSabores1 : MutableList<SaboresTabaco>
    lateinit var listaSabores2 : MutableList<SaboresTabaco>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevamezcla)
        val spinnerTabaco1 = findViewById<Spinner>(R.id.spinnertabaco1)
        val spinnerTabaco2 = findViewById<Spinner>(R.id.spinnertabaco2)
        val spinnerSabor = findViewById<Spinner>(R.id.spinnerSabor)
        val spinnerSabor1 = findViewById<Spinner>(R.id.spinnerSabor1)
        val spinnerSabor2 = findViewById<Spinner>(R.id.spinnerSabor2)


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


        spinnerTabaco1?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mezcla.tabaco1 = listaTabacos[position].nombre
                listaSabores1 = mutableListOf<SaboresTabaco>()
                db.collection("Sabores")
                    .whereEqualTo("idTabaco",listaTabacos[position].id)
                    .orderBy("nombre")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val sabor1: SaboresTabaco = SaboresTabaco()
                            sabor1.idSabor = document.id
                            sabor1.idTabaco = document.data["idTabaco"].toString()
                            sabor1.nombre = document.data["nombre"].toString()
                            sabor1.descripcion = document.data["descripcion"].toString()
                            listaSabores1.add(sabor1)
                        }
                        val customDropDownAdapterSabor =
                            parent?.let { SpinnerAdapterSaborMezclas(it.context, listaSabores1) }
                        spinnerSabor1.adapter = customDropDownAdapterSabor
                    }
            }
        }

        spinnerTabaco2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mezcla.tabaco2 = listaTabacos[position].nombre
                listaSabores2 = mutableListOf<SaboresTabaco>()
                db.collection("Sabores")
                    .whereEqualTo("idTabaco",listaTabacos[position].id)
                    .orderBy("nombre")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val sabor2: SaboresTabaco = SaboresTabaco()
                            sabor2.idSabor = document.id
                            sabor2.idTabaco = document.data["idTabaco"].toString()
                            sabor2.nombre = document.data["nombre"].toString()
                            sabor2.descripcion = document.data["descripcion"].toString()
                            listaSabores2.add(sabor2)
                        }
                        val customDropDownAdapterSabor =
                            parent?.let { SpinnerAdapterSaborMezclas(it.context, listaSabores2) }
                        spinnerSabor2.adapter = customDropDownAdapterSabor
                    }
            }
        }


        //SPINNER SABOR1

        spinnerSabor1?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mezcla.sabor1 = listaSabores1[position].nombre
            }
        }





        //FIN SPINNER SABOR 1



        //SPINNER SABOR 2
        spinnerSabor2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mezcla.sabor2 = listaSabores2[position].nombre
            }
        }

        //FIN SPINNER SABOR 2


        spinnerSabor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mezcla.sabor = listaSabores[position]
            }
        }

        btnEnviar = findViewById(R.id.btnNuevoSabor)
        edtPorcentaje1 = findViewById(R.id.edtPorcentaje1)
        edtPorcentaje2 = findViewById(R.id.edtPorcentaje2)


        btnEnviar.setOnClickListener() {
            if (mezcla.tabaco1.isEmpty()|| mezcla.tabaco2.isEmpty() || mezcla.sabor1.isEmpty() || mezcla.sabor2.isEmpty()
                || edtPorcentaje1.text.isEmpty() || edtPorcentaje2.text.isEmpty() || mezcla.sabor.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                db.collection("Mezclas").document().set(
                    hashMapOf("tabaco1" to mezcla.tabaco1,
                        "tabaco2" to mezcla.tabaco2,
                        "sabor1" to mezcla.sabor1.toString(),
                        "sabor2" to mezcla.sabor2.toString(),
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