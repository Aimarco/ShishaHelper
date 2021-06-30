package com.aimar.shishahelper.Activities

import Models.MarcaTabaco
import Models.MezclaTabaco
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aimar.shishahelper.R
import com.aimar.shishahelper.RecyclerView.TabacosRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomeActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
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
        mAuth = FirebaseAuth.getInstance()
        val mainLayout:CoordinatorLayout = findViewById(R.id.mainLayout)
        val nightModeFlags: Int = this.getResources().getConfiguration().uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> mainLayout.background = resources.getDrawable(R.drawable.fondoapp)
/*            Configuration.UI_MODE_NIGHT_NO -> doStuff()
            Configuration.UI_MODE_NIGHT_UNDEFINED -> doStuff()*/
        }


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
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mezclasMenu -> {
                val intent = Intent(this, MezclasActivity::class.java)
                this.startActivity(intent)
                true
            }
            R.id.logout -> {
                mAuth.signOut()
                val intent = Intent(this, AuthActivity::class.java)
                this.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
