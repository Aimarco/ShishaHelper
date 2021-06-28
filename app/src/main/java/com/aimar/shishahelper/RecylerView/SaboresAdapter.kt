package com.aimar.shishahelper.RecyclerView

import Models.MarcaTabaco
import Models.SaboresTabaco
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aimar.shishahelper.R
import com.squareup.picasso.Picasso

class SaboresAdapter : RecyclerView.Adapter<SaboresAdapter.ViewHolder>(){

        var tabacos: MutableList<SaboresTabaco>  = ArrayList()
        lateinit var context: Context
        fun RecyclerAdapter(superheros : MutableList<SaboresTabaco>, context: Context){
            this.tabacos = superheros
            this.context = context
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = tabacos.get(position)
            holder.bind(item, context)
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
            fun bind(sabor: SaboresTabaco, context: Context){
                nombreSabor.text = sabor.nombre
                descSabor.text = sabor.descripcion
            }
        }
}