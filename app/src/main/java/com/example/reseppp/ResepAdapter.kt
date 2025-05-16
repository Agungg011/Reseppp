package com.example.reseppp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ResepAdapter(
    private val resepList: List<MainActivity.ResepMakan>
) : RecyclerView.Adapter<ResepAdapter.ResepViewHolder>() {

    inner class ResepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaResep: TextView = itemView.findViewById(R.id.namaResepText)
        val gambarResep: ImageView = itemView.findViewById(R.id.gambarResepImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resep, parent, false)
        return ResepViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        val resep = resepList[position]

        holder.namaResep.text = resep.namaResep

        Glide.with(holder.itemView.context)
            .load(resep.gambar)
            .into(holder.gambarResep)

        // Ketika gambar diklik, buka DetailActivity berdasarkan ID
        holder.gambarResep.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("resep_id", resep.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = resepList.size

    // Digunakan untuk pencarian
    fun updateData(newList: List<MainActivity.ResepMakan>) {
        (resepList as MutableList).clear()
        resepList.addAll(newList)
        notifyDataSetChanged()
    }
}
