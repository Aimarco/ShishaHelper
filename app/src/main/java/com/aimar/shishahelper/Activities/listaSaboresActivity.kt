package com.aimar.shishahelper.Activities

import Models.MarcaTabaco
import Models.SaboresTabaco
import Utils.Utils
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aimar.shishahelper.R
import com.aimar.shishahelper.RecyclerView.RecyclerAdapter
import com.aimar.shishahelper.RecyclerView.SaboresAdapter
import com.aimar.shishahelper.RecyclerView.TabacosRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.io.Serializable

class listaSaboresActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private val db=FirebaseFirestore.getInstance()
    lateinit var mRecyclerView : RecyclerView
    val mAdapter : SaboresAdapter = SaboresAdapter()
    lateinit var addSabor : FloatingActionButton
    lateinit var listaSabores : MutableList<SaboresTabaco>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listasabores)
        // Get the Intent that started this activity and extract the string
       val  intent: Intent = getIntent();
        mAuth = FirebaseAuth.getInstance()

        val sabor :MarcaTabaco  = intent.getSerializableExtra("tabacoId") as MarcaTabaco
        val listaTabacos = mutableListOf<MarcaTabaco>()
        listaSabores = mutableListOf()

        val imgTabaco = findViewById<ImageView>(R.id.imgMini)


        //llamada sabores
        db.collection("Sabores")
            .whereEqualTo("idTabaco", sabor.id).orderBy("nombre")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val sabor: SaboresTabaco = SaboresTabaco()
                    sabor.idSabor = document.id
                    sabor.idTabaco = document.data["idTabaco"].toString()
                    sabor.nombre = document.data["nombre"].toString()
                    sabor.descripcion = document.data["descripcion"].toString()
                    listaSabores.add(sabor)
                }
                setUpRecyclerView(mAuth.currentUser?.email.toString())
            }

        //FIN llamada sabores
        imgTabaco.setImageResource(Utils.convertImage(sabor.nombre))
        //Picasso.with(this).load(sabor.imagen).into(imgTabaco)

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.txtSabor)
            textView.text = "Sabores de "+sabor.nombre


        //db.insertTabacos()
        addSabor = findViewById(R.id.addSabor)
        addSabor.setOnClickListener {
            val intent :Intent = Intent(this, AddSaborActivity::class.java)
            intent.putExtra("tabacoId",sabor)
            this.startActivity(intent)

        }


    }

    fun setUpRecyclerView(email:String){

        mRecyclerView = findViewById(R.id.recyclerViewSabores) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = GridLayoutManager(this,1)
        mAdapter.RecyclerAdapter(listaSabores, this,email)
        mRecyclerView.adapter = mAdapter

    }
}