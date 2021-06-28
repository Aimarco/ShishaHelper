package com.aimar.shishahelper.RecyclerView

import Models.MarcaTabaco
import Utils.CircleTransform
import Utils.RoundedCornersTransform
import Utils.Utils
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aimar.shishahelper.Activities.listaSaboresActivity
import com.aimar.shishahelper.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


class TabacosRecyclerAdapter : RecyclerView.Adapter<TabacosRecyclerAdapter.ViewHolder>(){
    var tabacos: MutableList<MarcaTabaco>  = ArrayList()
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var itemImage: ImageView
            var itemTitle: TextView

            init {
                itemImage = itemView.findViewById(R.id.imgTabaco)
                itemTitle = itemView.findViewById(R.id.nombreTabaco)
            }
        }

    fun TabacosAdapter(tabacosList: MutableList<MarcaTabaco>){
        tabacos = tabacosList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cardtabacos, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = tabacos[i].nombre
        /*Picasso.with(viewHolder.itemImage.context).load(tabacos[i].imagen).transform(RoundedCornersTransformation(10,10)).resize(150,150).into(
            viewHolder.itemImage)*/
        viewHolder.itemImage.setImageResource(Utils.convertImage(tabacos[i].nombre))
        viewHolder.itemImage.setOnClickListener(View.OnClickListener {
            val intent : Intent = Intent(viewHolder.itemImage.context, listaSaboresActivity::class.java).apply {
                val listaTabacos = arrayListOf<MarcaTabaco>()
                listaTabacos.addAll(tabacos)
                putExtra("tabacoId",tabacos[i])
            }
            viewHolder.itemImage.context.startActivity(intent)})


    }

    override fun getItemCount(): Int {
        return tabacos.size
    }

}
