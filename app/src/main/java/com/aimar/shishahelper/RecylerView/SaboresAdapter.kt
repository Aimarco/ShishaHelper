package com.aimar.shishahelper.RecyclerView

import Models.SaboresTabaco
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aimar.shishahelper.Activities.HomeActivity
import com.aimar.shishahelper.Activities.MezclasActivity
import com.aimar.shishahelper.R
import com.google.firebase.firestore.FirebaseFirestore


class SaboresAdapter : RecyclerView.Adapter<SaboresAdapter.ViewHolder>(){

    var tabacos: MutableList<SaboresTabaco>  = ArrayList()
        lateinit var email:String
        lateinit var context: Context
        fun RecyclerAdapter(superheros : MutableList<SaboresTabaco>, context: Context, email:String){
            this.tabacos = superheros
            this.context = context
            this.email = email
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = tabacos.get(position)
            if(position %2 == 1)
            {
                holder.itemView.setBackgroundDrawable(context.resources.getDrawable(R.drawable.spinnergreen))
            }
            else
            {
                holder.itemView.setBackgroundDrawable(context.resources.getDrawable(R.drawable.spinnercyan))
            }
            holder.bind(item, context,email)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return ViewHolder(layoutInflater.inflate(R.layout.saboresrecyclerview, parent, false))
        }
        override fun getItemCount(): Int {
            return tabacos.size
        }
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nombreSabor = view.findViewById(R.id.nombre) as TextView
            val descSabor = view.findViewById(R.id.descripcion) as TextView
            val imgTrash = view.findViewById(R.id.trashCan) as ImageView
            val linearSabor = view.findViewById(R.id.linearSabor) as LinearLayout


            fun bind(sabor: SaboresTabaco, context: Context,email:String){
                nombreSabor.text = sabor.nombre
                descSabor.text = sabor.descripcion
                nombreSabor.setTextColor(context.getColor(R.color.black))
                descSabor.setTextColor(context.getColor(R.color.black))

                if((email.equals(context.getString(R.string.adminAimar),true)) || (email.equals(context.getString(R.string.adminRaul),true))){
                    imgTrash.setImageResource(R.drawable.basura)
                    val db = FirebaseFirestore.getInstance()
                    imgTrash.setOnClickListener {
                        db.collection("Sabores")
                            .document(sabor.idSabor)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Se ha borrado el sabor: "+sabor.nombre, Toast.LENGTH_SHORT)
                                    .show()
                                context.startActivity(Intent(context, HomeActivity::class.java))
                                (context as Activity).finish()

                            }
                            .addOnFailureListener { Toast.makeText(context, "Ha ocurrido un problema.", Toast.LENGTH_SHORT)
                                .show() }
                    }

                }else{
                    imgTrash.setImageResource(R.drawable.rightarrow)
                }


                linearSabor.setOnClickListener {
                    val intent:Intent = Intent(context, MezclasActivity::class.java)
                    intent.putExtra("tabacoId",sabor)
                    context.startActivity(intent)
                }
            }
        }
}