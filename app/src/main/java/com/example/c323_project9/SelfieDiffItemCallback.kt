package com.example.c323_project9

import androidx.recyclerview.widget.DiffUtil

class SelfieDiffItemCallback : DiffUtil.ItemCallback<Selfie>() {
    override fun areItemsTheSame(oldItem: Selfie, newItem: Selfie)
            = (oldItem.selfieId == newItem.selfieId)
    override fun areContentsTheSame(oldItem: Selfie, newItem: Selfie) = (oldItem == newItem)
}