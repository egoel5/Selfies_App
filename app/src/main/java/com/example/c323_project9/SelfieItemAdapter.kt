package com.example.c323_project9

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.c323_project9.databinding.SelfieItemBinding

class SelfieItemAdapter(val clickListener: (noteId: Long) -> Unit)
    : ListAdapter<Selfie, SelfieItemAdapter.NoteItemViewHolder>(SelfieDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : NoteItemViewHolder = NoteItemViewHolder.inflateFrom(parent)
    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class NoteItemViewHolder(val binding: SelfieItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): NoteItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SelfieItemBinding.inflate(layoutInflater, parent, false)
                return NoteItemViewHolder(binding)
            }
        }

        fun bind(item: Selfie, clickListener: (selfieId: Long) -> Unit) {
            binding.selfie = item
            binding.root.setOnClickListener { clickListener(item.selfieId.toLong()) }
        }
    }
}