package com.example.c323_project9

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.c323_project9.databinding.SelfieItemBinding
import kotlin.reflect.KFunction1

class SelfieItemAdapter(val context: Context)
    : ListAdapter<Selfie, SelfieItemAdapter.SelfieItemViewHolder>(SelfieDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : SelfieItemViewHolder = SelfieItemViewHolder.inflateFrom(parent)
    override fun onBindViewHolder(holder: SelfieItemViewHolder, position: Int) {
        val item = getItem(position)
        Log.v("obvh", item.selfieUrl)
        holder.bind(item, context)
    }

    class SelfieItemViewHolder(val binding: SelfieItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): SelfieItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SelfieItemBinding.inflate(layoutInflater, parent, false)
                return SelfieItemViewHolder(binding)
            }
        }

        fun bind(item: Selfie, context: Context) {
            binding.selfie = item
            Log.v("bind", item.selfieUrl)
            Glide.with(context).load(item.selfieUrl).into(binding.imgSelfie)
        }
    }
}