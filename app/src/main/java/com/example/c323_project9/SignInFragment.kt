package com.example.c323_project9

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.c323_project9.databinding.FragmentSignInBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    val TAG = "SignInFragment"
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : SelfieViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInFragment_to_mainFragment)
                viewModel.onNavigatedToList()
            }
        })

        // navigate to sign up page
        viewModel.navigateToSignUp.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInFragment_to_signUpFragment)
                viewModel.onNavigatedToSignUp()
            }
        })
        // send a toast if something goes wrong
        viewModel.errorHappened.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })
        binding.nextBtn.setOnClickListener {
            viewModel.signIn(binding.emailEt.text.toString(), binding.passEt.text.toString())
        }

        return view
    }
}