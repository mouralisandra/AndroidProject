package com.example.ProjetAndroid.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.ProjetAndroid.databinding.FragmentLoginBinding
import com.example.ProjetAndroid.View.VNsApplication
import com.example.ProjetAndroid.ViewModel.viewmodels.LoginState
import com.example.ProjetAndroid.ViewModel.viewmodels.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.example.ProjetAndroid.R

class LoginFragment : Fragment() {

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("LoginFragment == null")

    private val component by lazy {
        (requireActivity().application as VNsApplication).component
    }

    private val userViewModelFactory by lazy {
        component.userViewModelFactory()
    }

    private val userViewModel by lazy {
        ViewModelProvider(requireActivity(), userViewModelFactory)[UserViewModel::class.java]
    }

    private lateinit var savedStateHandle: SavedStateHandle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_empty) // Replace with your GIF resource
            .into(binding.gifImage)
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle[LOGIN_SUCCESSFUL] = false
        binding.sendToken.setOnClickListener {
            val token = binding.textInputEditText.text.toString()
            userViewModel.login(token)
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                userViewModel.loginState.collectLatest {
                    binding.state.text = it.state.toString()
                    if (it.state == LoginState.Logged) {
                        savedStateHandle[LOGIN_SUCCESSFUL] = true
                        findNavController().popBackStack()
                    }
                    else if (it.state == LoginState.Error) {
                        binding.state.text = "Authentification error, Please enter the correct Token"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}