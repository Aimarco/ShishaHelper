package Utils

import Models.MarcaTabaco
import Models.SaboresTabaco
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.aimar.shishahelper.R
import com.google.firebase.firestore.FirebaseFirestore

object Utils {

    fun convertImage(nombre:String) : Int {
        var nombreReturn = 0
        when (nombre.toLowerCase().trim()) {
            "adalya" -> nombreReturn = R.drawable.adalya
            "blue horse" -> nombreReturn = R.drawable.bluehorse
            "overdozz" -> nombreReturn = R.drawable.overdozz
            "privilege" -> nombreReturn = R.drawable.privilege
            "hookain" ->    nombreReturn = R.drawable.hookain
            "dozaj" -> nombreReturn = R.drawable.dozaj
            "al fakher" -> nombreReturn = R.drawable.alfaker
            "serbetli" -> nombreReturn = R.drawable.serbetli
            "al waha" -> nombreReturn = R.drawable.alwaha
            "mezclas" -> nombreReturn = R.drawable.mezclas
        }
        return nombreReturn
    }

    fun getTabacos(db:FirebaseFirestore):MutableList<MarcaTabaco>{
        val listaTabacos = mutableListOf<MarcaTabaco>()
        db.collection("Tabacos")
            .orderBy("name")
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

            }
            .addOnFailureListener { exception ->
                Log.d("ERROR", "Error getting documents: ", exception)
            }
        return listaTabacos
    }
    fun getSabores(db:FirebaseFirestore,saborId:String):MutableList<SaboresTabaco>{
        val listaSabores = mutableListOf<SaboresTabaco>()

        db.collection("Sabores")
            .whereEqualTo("idTabaco", saborId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val sabor: SaboresTabaco = SaboresTabaco()
                    sabor.idTabaco = document.data["idTabaco"].toString()
                    sabor.nombre = document.data["nombre"].toString()
                    sabor.descripcion = document.data["descripcion"].toString()
                    listaSabores.add(sabor)
                }
            }
        return listaSabores
    }


    fun showAlertErrorRequest(context:Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error en la request")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

}