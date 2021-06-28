package com.aimar.shishahelper.Activities

import Models.MarcaTabaco
import Models.MezclaTabaco
import Models.SaboresTabaco
import Utils.Utils
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aimar.shishahelper.R
import com.aimar.shishahelper.RecyclerView.MezclasAdapter
import com.aimar.shishahelper.RecyclerView.SaboresAdapter
import com.aimar.shishahelper.RecyclerView.SpinnerAdapter
import com.aimar.shishahelper.RecyclerView.SpinnerAdapterSabor
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class MezclasActivity : AppCompatActivity() {
    private val db= FirebaseFirestore.getInstance()
    lateinit var mRecyclerView : RecyclerView
    val mAdapter : MezclasAdapter = MezclasAdapter()
    lateinit var listaMezclas : MutableList<MezclaTabaco>
    lateinit var saborSeleccionado : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mezclasview)
        // Get the Intent that started this activity and extract the string
        val  intent: Intent = getIntent();
        val spinnerSabor = findViewById<Spinner>(R.id.spinnerSabores)
        val listaSabores:ArrayList<String> = ArrayList()
        listaSabores.add("Todos")
        listaSabores.add("Citrico")
        listaSabores.add("Dulce")
        listaSabores.add("Afrutado")
        val customDropDownAdapter = SpinnerAdapterSabor(this, listaSabores)
        spinnerSabor.adapter = customDropDownAdapter
        listaMezclas = mutableListOf()
        val btnAniadirMezcla:FloatingActionButton
        btnAniadirMezcla = findViewById(R.id.aniadirMezcla)
        btnAniadirMezcla.setOnClickListener {
            startActivity(Intent(this, AddMezclaActivity::class.java))
            finish()
        }

        spinnerSabor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                saborSeleccionado = listaSabores[position]

                if (saborSeleccionado.equals("Todos",true)){
                    listaMezclas = mutableListOf()
                    db.collection("Mezclas")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val mezcla: MezclaTabaco = MezclaTabaco()
                                mezcla.sabor1 = document.data["sabor1"].toString()
                                mezcla.sabor2 = document.data["sabor2"].toString()
                                mezcla.tabaco1 = document.data["tabaco1"].toString()
                                mezcla.tabaco2 = document.data["tabaco2"].toString()
                                mezcla.sabor = document.data["sabor"].toString()
                                mezcla.porcentaje1 = Integer.parseInt(document.data["porcentaje1"].toString())
                                mezcla.porcentaje2 = Integer.parseInt(document.data["porcentaje2"].toString())
                                listaMezclas.add(mezcla)
                            }
                            setUpRecyclerView()
                        }
                }else{
                    listaMezclas = mutableListOf()
                    db.collection("Mezclas")
                        .whereEqualTo("sabor", saborSeleccionado)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val mezcla: MezclaTabaco = MezclaTabaco()
                                mezcla.sabor1 = document.data["sabor1"].toString()
                                mezcla.sabor2 = document.data["sabor2"].toString()
                                mezcla.tabaco1 = document.data["tabaco1"].toString()
                                mezcla.tabaco2 = document.data["tabaco2"].toString()
                                mezcla.sabor = document.data["sabor"].toString()
                                mezcla.porcentaje1 = Integer.parseInt(document.data["porcentaje1"].toString())
                                mezcla.porcentaje2 = Integer.parseInt(document.data["porcentaje2"].toString())
                                listaMezclas.add(mezcla)
                            }
                            setUpRecyclerView()
                        }
                }

                //Picasso.with(imagenHeader.context).load(listaTabacos[position].imagen).into(imagenHeader)
            }
        }
        //val db = DataDbHelper(this)
        //listaMezclas=db.getMezclas()

        setUpRecyclerView()

    }

    fun setUpRecyclerView(){

        mRecyclerView = findViewById(R.id.recyclerViewMezclas) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = GridLayoutManager(this,1)
        mAdapter.RecyclerAdapter(listaMezclas, this)
        mRecyclerView.adapter = mAdapter

    }


}