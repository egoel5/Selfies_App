package com.example.c323_project9

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.c323_project9.databinding.FragmentMainScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 * Use the [MainScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainScreenFragment : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    lateinit var selfiesList: ArrayList<Selfie>
    lateinit var firebaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : SelfieViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.selfiesList.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("images")
        selfiesList = arrayListOf()

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (selfieSnap in snapshot.children){
                    val selfies = selfieSnap.getValue(Selfie::class.java)
                    selfiesList.add(selfies!!)
                }
                val adapter = SelfieItemAdapter(this@MainScreenFragment.requireContext(), selfiesList)
                binding.selfiesList.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        binding.btnTakePhoto.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_takePhotoFragment)
            viewModel.onSelfieNavigated()
        }

        // navigate to EditNote Fragment
        viewModel.navigateToSelfie.observe(viewLifecycleOwner, Observer { selfieId ->
            selfieId?.let {
                view.findNavController()
                    .navigate(R.id.action_mainFragment_to_selfieFragment)
                viewModel.onSelfieNavigated()
            }
        })
        return view
    }
}