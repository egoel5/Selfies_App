package com.example.c323_project9

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.c323_project9.databinding.FragmentSelfieBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.bumptech.glide.Glide

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [SelfieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelfieFragment : Fragment() {
    private var _binding: FragmentSelfieBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelfieBinding.inflate(inflater, container, false)
        val view = binding.root

        val storageReference = Firebase.storage.reference
        Glide.with(this)
            .load(storageReference)
            .into(binding.imgSelfieFull)
        return view
    }
}