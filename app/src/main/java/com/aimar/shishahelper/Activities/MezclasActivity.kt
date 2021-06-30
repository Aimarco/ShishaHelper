package com.aimar.shishahelper.Activities

import Models.MezclaTabaco
import Models.SaboresTabaco
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aimar.shishahelper.R
import com.aimar.shishahelper.RecyclerView.MezclasAdapter
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
        val sabor: SaboresTabaco? = intent.getSerializableExtra("tabacoId") as SaboresTabaco?
        val txtSabor : TextView = findViewById(R.id.txtNombreSabor)
        var verTodas:Boolean
        val switchTodas: Switch = findViewById(R.id.switch1)
        val filtroSabor:LinearLayout = findViewById(R.id.linearSpinnersaborMezcla)
        if(sabor!=null){
            txtSabor.text = sabor?.nombre.toString()
            verTodas = false
        }else{
            txtSabor.text = "Todas"
            verTodas=true
            switchTodas.isChecked = true
            filtroSabor.visibility = View.VISIBLE
        }

        val spinnerSabor = findViewById<Spinner>(R.id.spinnerSabores)
        val listaSabores:ArrayList<String> = ArrayList()
        listaSabores.add("Todos")
        listaSabores.add("Citrico")
        listaSabores.add("Dulce")
        listaSabores.add("Afrutado")
        val customDropDownAdapter = SpinnerAdapterSabor(this, listaSabores)
        spinnerSabor.adapter = customDropDownAdapter




        spinnerSabor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                saborSeleccionado = listaSabores[position]
                if (!verTodas) {
                    loadData(sabor?.nombre.toString())
                }else if((verTodas) && saborSeleccionado.equals("todos",true)) {
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
                                mezcla.porcentaje1 =
                                    Integer.parseInt(document.data["porcentaje1"].toString())
                                mezcla.porcentaje2 =
                                    Integer.parseInt(document.data["porcentaje2"].toString())
                                listaMezclas.add(mezcla)
                            }
                            setUpRecyclerView()
                        }
                        }else if((verTodas) && !(saborSeleccionado.equals("todos", true))){
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
                                    mezcla.porcentaje1 =
                                        Integer.parseInt(document.data["porcentaje1"].toString())
                                    mezcla.porcentaje2 =
                                        Integer.parseInt(document.data["porcentaje2"].toString())
                                    listaMezclas.add(mezcla)
                                }
                                setUpRecyclerView()
                            }
                    }
                }
            }



        //VER TODAS MEZCLAS
        switchTodas.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            if(isChecked){
                verTodas = true
                filtroSabor.visibility = View.VISIBLE
                showAlert()

            }else{
                filtroSabor.visibility = View.INVISIBLE
                verTodas = false
                loadData(sabor?.nombre.toString())

            }

        })
        //FIN SWITCH1
        val btnAniadirMezcla:FloatingActionButton
        btnAniadirMezcla = findViewById(R.id.aniadirMezcla)
        btnAniadirMezcla.setOnClickListener {
            startActivity(Intent(this, AddMezclaActivity::class.java))
            finish()
        }


    }

    fun setUpRecyclerView(){

        mRecyclerView = findViewById(R.id.recyclerViewMezclas) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = GridLayoutManager(this,1)
        mAdapter.RecyclerAdapter(listaMezclas, this)
        mRecyclerView.adapter = mAdapter

    }

    private fun showAlert(){
        listaMezclas = mutableListOf()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuidado!")
        builder.setMessage("Seguro que quieres mostrar TODAS las mezclas?")
        builder.setPositiveButton("Aceptar")
        { dialog, which ->
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
                        mezcla.porcentaje1 =
                            Integer.parseInt(document.data["porcentaje1"].toString())
                        mezcla.porcentaje2 =
                            Integer.parseInt(document.data["porcentaje2"].toString())
                        listaMezclas.add(mezcla)
                    }
                    setUpRecyclerView()
                }
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

        private fun loadData(saborid:String) {
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
                        mezcla.porcentaje1 =
                            Integer.parseInt(document.data["porcentaje1"].toString())
                        mezcla.porcentaje2 =
                            Integer.parseInt(document.data["porcentaje2"].toString())
                        if ((saborid.equals(document.data["sabor1"].toString(),true)) || (saborid.equals(document.data["sabor2"].toString(), true))) {
                            listaMezclas.add(mezcla)
                        }
                    }
                    setUpRecyclerView()
                }

        }
}