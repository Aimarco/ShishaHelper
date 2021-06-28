package com.aimar.shishahelper.Activities

import Models.MarcaTabaco
import Models.SaboresTabaco
import Utils.Utils
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aimar.shishahelper.R
import com.aimar.shishahelper.RecyclerView.SpinnerAdapter
import com.google.firebase.firestore.FirebaseFirestore


class AddSaborActivity  : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    lateinit var listaTabacos:MutableList<MarcaTabaco>
    lateinit var btnEnviar : Button
    lateinit var editDescSabor : EditText
    lateinit var editNameSabor : EditText
    lateinit var idTabacoSeleccionado : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevosabor)
        val spinnerTabacos = findViewById<Spinner>(R.id.spinnertabacos)
        val  intent: Intent = getIntent();
        listaTabacos = mutableListOf()
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
                spinnerTabacos.adapter = customDropDownAdapter
            }
            .addOnFailureListener { exception ->
                Log.d("ERROR", "Error getting documents: ", exception)
            }

        //fin carga tabacos


        spinnerTabacos?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var imagenHeader = findViewById<ImageView>(R.id.imgHeader)
                var txtHeader = findViewById<TextView>(R.id.txtHeader)
                idTabacoSeleccionado = listaTabacos[position].id
                txtHeader.text= listaTabacos[position].nombre
                imagenHeader.setImageResource(Utils.convertImage(listaTabacos[position].nombre))
                //Picasso.with(imagenHeader.context).load(listaTabacos[position].imagen).into(imagenHeader)
            }
        }
        btnEnviar = findViewById(R.id.btnNuevoSabor)
        editDescSabor = findViewById(R.id.edtDescSabor)
        editNameSabor = findViewById(R.id.edtNombreSabor)

        btnEnviar.setOnClickListener() {
            if (editNameSabor.text.isEmpty() || editDescSabor.text.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                var saborNuevo: SaboresTabaco = SaboresTabaco()
                saborNuevo.nombre = editNameSabor.text.toString()
                saborNuevo.descripcion = editDescSabor.text.toString()
                db.collection("Sabores").document().set(
                    hashMapOf("nombre" to editNameSabor.text.toString(),
                    "descripcion" to editDescSabor.text.toString(),
                    "idTabaco" to idTabacoSeleccionado)
                )
                Toast.makeText(this, "Nuevo sabor añadido con éxito! Gracias!", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

}