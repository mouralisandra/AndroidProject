package com.example.ProjetAndroid.View.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.ProjetAndroid.R
import com.example.ProjetAndroid.databinding.FragmentLoginBinding
import com.example.ProjetAndroid.View.AndroidPeojectApplication
import com.example.ProjetAndroid.ViewModel.viewmodels.LoginState
import com.example.ProjetAndroid.ViewModel.viewmodels.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.example.ProjetAndroid.NetworkUtils
import kotlinx.coroutines.cancel

class LoginFragment : Fragment() {

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("LoginFragment binding is null")

    private val component by lazy {
        (requireActivity().application as AndroidPeojectApplication).component
    }

    private val userViewModelFactory by lazy {
        component.userViewModelFactory()
    }

    private val userViewModel by lazy {
        ViewModelProvider(requireActivity(), userViewModelFactory)[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
        }
        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_empty )
            .into(binding.gifImage)

        binding.sendToken.setOnClickListener {
            val token = binding.textInputEditText.text.toString()
            userViewModel.login(token)
        }

        lifecycleScope.launch {
            try {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    userViewModel.loginState.collectLatest { loginState ->
                        binding.state.text = loginState.state.toString()
                        if (loginState.state == LoginState.Logged) {
                            findNavController().navigate(R.id.action_loginFragment_to_vnListFragment)
                        } else if (loginState.state == LoginState.Error) {
                            binding.state.text = "Authentication error, Please enter the correct Token"
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginFragment", "Error in coroutine: ${e.message}", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.cancel()
        _binding = null
    }
}
