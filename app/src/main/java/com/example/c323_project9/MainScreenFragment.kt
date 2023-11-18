package com.example.c323_project9

import android.os.Bundle
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

        val selfieList = binding.selfiesList
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        selfieList.layoutManager = staggeredGridLayoutManager

        fun selfieClicked (selfie : Selfie) {
            viewModel.onSelfieClicked(selfie)
        }

        // initialize and set adapter
        val adapter = SelfieItemAdapter(this.requireContext(), ::selfieClicked)
        binding.selfiesList.adapter = adapter

        // when a notes item is observed, submitList of notes to the adapter
        viewModel.selfies.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
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