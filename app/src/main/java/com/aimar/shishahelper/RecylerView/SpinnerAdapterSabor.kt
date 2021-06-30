package com.aimar.shishahelper.RecyclerView

import Models.MarcaTabaco
import Models.SaboresTabaco
import Utils.Utils
import com.aimar.shishahelper.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class SpinnerAdapterSabor(val context: Context, var dataSource: ArrayList<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.simpleadapter, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        if(dataSource.get(position).equals("citrico",true)){
            view.setBackgroundColor(context.resources.getColor(R.color.citrico))
        }else if(dataSource.get(position).equals("dulce",true)){
            view.setBackgroundColor(context.resources.getColor(R.color.dulce))

        }else if(dataSource.get(position).equals("afrutado",true)){
            view.setBackgroundColor(context.resources.getColor(R.color.afrutado))
        }else{
            view.setBackgroundColor(context.resources.getColor(R.color.todosbg))
        }


        vh.label.text = dataSource.get(position)
        vh.label.setTextColor(context.getColor(R.color.white))

        //Picasso.with(view.context).load(dataSource[position].imagen).into(vh.img)
        //vh.img.setImageResource(Utils.convertImage(dataSource.get(position)))

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView

        init {
            label = row?.findViewById(R.id.spinnername) as TextView
        }
    }



}