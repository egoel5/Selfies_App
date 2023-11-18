package com.example.c323_project9

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.c323_project9.databinding.SelfieItemBinding
import kotlin.reflect.KFunction1

class SelfieItemAdapter(val context: Context, val clickListener: KFunction1<Selfie, Unit>)
    : ListAdapter<Selfie, SelfieItemAdapter.SelfieItemViewHolder>(SelfieDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : SelfieItemViewHolder = SelfieItemViewHolder.inflateFrom(parent)
    override fun onBindViewHolder(holder: SelfieItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context, clickListener)
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

        fun bind(item: Selfie, context: Context, clickListener: KFunction1<Selfie, Unit>) {
            binding.selfie = item
            Glide.with(context).load(item.selfieUrl).into(binding.imgSelfie)
            binding.root.setOnClickListener { clickListener(item) }
        }
    }
}