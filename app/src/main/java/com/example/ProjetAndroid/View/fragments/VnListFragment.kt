package com.example.ProjetAndroid.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.ProjetAndroid.NetworkUtils
import com.example.ProjetAndroid.R
import com.example.ProjetAndroid.databinding.FragmentVnListBinding
import com.example.ProjetAndroid.View.VndbApplication
import com.example.ProjetAndroid.View.adapters.VnListAdapter
import com.example.ProjetAndroid.View.adapters.VnLoadStateAdapter
import com.example.ProjetAndroid.ViewModel.viewmodels.LoginState
import com.example.ProjetAndroid.ViewModel.viewmodels.UserViewModel
import com.example.ProjetAndroid.ViewModel.viewmodels.lazyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VnListFragment : Fragment() {

    private var _binding: FragmentVnListBinding? = null
    private val binding: FragmentVnListBinding
        get() = _binding ?: throw RuntimeException("VnListFragment == null")

    private val component by lazy {
        (requireActivity().application as VndbApplication).component
    }

    private val viewModel by lazyViewModel { stateHandle ->
        component.vnListViewModel().create(stateHandle)
    }

    private val userViewModelFactory by lazy {
        component.userViewModelFactory()
    }

    private val userViewModel by lazy {
        ViewModelProvider(requireActivity(), userViewModelFactory)[UserViewModel::class.java]
    }

    private val vnListAdapter by lazy {
        VnListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVnListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
        }

        // Setup night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupToolbar()
        observeLoginState()
        setupList()
    }

    private fun setupToolbar() {
        binding.myToolbar.inflateMenu(R.menu.top_app_bar)

        binding.myToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    userViewModel.logout()
                    true
                }
                R.id.action_profile -> {
                    findNavController().navigate(VnListFragmentDirections.actionVnListFragmentToUserPageFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.loginState.collectLatest { loginState ->
                    when (loginState.state) {
                        LoginState.NotLogged -> {
                            findNavController().navigate(R.id.action_global_loginFragment)
                        }
                        LoginState.Logged -> {
                            binding.myToolbar.title = loginState.user?.username
                            binding.myToolbar.menu.findItem(R.id.action_profile).isVisible = true
                            binding.myToolbar.menu.findItem(R.id.action_logout).isVisible = true
                        }
                        LoginState.Logging, LoginState.Error -> {
                        }
                    }
                }
            }
        }
    }


    private fun setupList() {
        vnListAdapter.onVnClickListener = { vn ->
            findNavController().navigate(
                VnListFragmentDirections.actionVnListFragmentToVnDetailsFragment(vn.id)
            )
        }

        binding.retryButton.setOnClickListener { vnListAdapter.retry() }
        val header = VnLoadStateAdapter { vnListAdapter.retry() }
        binding.vnList.adapter = vnListAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = VnLoadStateAdapter { vnListAdapter.retry() }
        )

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vnList.collectLatest { pagingData ->
                    vnListAdapter.submitData(pagingData)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vnListAdapter.loadStateFlow.collect { loadState ->
                    // Update UI based on LoadState
                    binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                    binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error
                    binding.emptyList.isVisible = loadState.refresh is LoadState.NotLoading && vnListAdapter.itemCount == 0
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
