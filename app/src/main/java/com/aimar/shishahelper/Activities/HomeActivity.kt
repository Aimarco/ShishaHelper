package com.aimar.shishahelper.Activities

import Models.MarcaTabaco
import Models.MezclaTabaco
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aimar.shishahelper.R
import com.aimar.shishahelper.RecyclerView.TabacosRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class HomeActivity : AppCompatActivity() {
    lateinit var mRecyclerView : RecyclerView
    val mAdapter : TabacosRecyclerAdapter = TabacosRecyclerAdapter()
    lateinit var fabmenu : FloatingActionButton
    lateinit var fabtabaco : FloatingActionButton
    lateinit var fabmezcla : FloatingActionButton
    private val db=FirebaseFirestore.getInstance()
    lateinit var listaTabacos : MutableList<MarcaTabaco>
    lateinit var listaMezclas : MutableList<MezclaTabaco>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listaTabacos = mutableListOf()


        //PETICION TABACOS
        db.collection("Tabacos")
            .orderBy("nombre")
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
                setUpRecyclerView()
            }
            .addOnFailureListener { exception ->
                Log.d("ERROR", "Error getting documents: ", exception)
            }

        //fin peticion tabacos

        //System.out.println("NUMERO DE Tabacos: "+ listaTabacos.size)

        //listaMezclas = db.getMezclas()
//        System.out.println("NUMERO DE MEZCLAS: "+ listaMezclas.size)



    }


    fun setUpRecyclerView(){

        mRecyclerView = findViewById(R.id.recyclerViewTabacos) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = GridLayoutManager(this,2)
        mAdapter.TabacosAdapter(listaTabacos)
        mRecyclerView.adapter = mAdapter

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    fun runMezclasIntent(context : Context){

        val intent = Intent(context, MezclasActivity::class.java)
        context.startActivity(intent)

    }

}
