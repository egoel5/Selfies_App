package com.example.c323_project9

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.c323_project9.databinding.SelfieItemBinding
import kotlin.reflect.KFunction1

class SelfieItemAdapter(val context: Context, val selfies: List<Selfie>)
    : RecyclerView.Adapter<SelfieViewHolder>() {

    override fun getItemCount(): Int {
        return selfies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfieViewHolder {
        return SelfieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.selfie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SelfieViewHolder, position: Int) {
        val selfie = selfies[position]
        Glide.with(context)
            .load(selfie.selfieUrl)
            .into(holder.imgSelfie)
    }

}

class SelfieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imgSelfie: ImageView = itemView.findViewById(R.id.img_selfie)
}
