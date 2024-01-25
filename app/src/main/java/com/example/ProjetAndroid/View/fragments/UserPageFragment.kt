package com.example.ProjetAndroid.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.ProjetAndroid.NetworkUtils
import com.example.ProjetAndroid.R
import com.example.ProjetAndroid.databinding.FragmentUserPageBinding
import com.example.ProjetAndroid.View.VndbApplication
import com.example.ProjetAndroid.ViewModel.viewmodels.LoginState
import com.example.ProjetAndroid.ViewModel.viewmodels.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserPageFragment : Fragment() {

    private var _binding: FragmentUserPageBinding? = null
    private val binding: FragmentUserPageBinding
        get() = _binding ?: throw RuntimeException("UserPageFragment == null")

    private val component by lazy {
        (requireActivity().application as VndbApplication).component
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
        _binding = FragmentUserPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
        }
        val navController = findNavController()
        binding.poster.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.profile))

        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LoginFragment.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry, Observer { success ->
                if (!success) {
                    val startDestination = navController.graph.startDestinationId
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build()
                    navController.navigate(startDestination, null, navOptions)
                }
            })

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                userViewModel.loginState.collectLatest {
                    if (it.state != LoginState.Logged) {
                        findNavController().navigate(R.id.loginFragment)
                    }
                    if (it.state == LoginState.Logged) {
                        binding.userName.text = it.user?.username
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