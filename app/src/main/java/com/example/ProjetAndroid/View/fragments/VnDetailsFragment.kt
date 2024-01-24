package com.example.ProjetAndroid.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.ProjetAndroid.R
import com.example.ProjetAndroid.databinding.FragmentVnDetailsBinding
import com.example.ProjetAndroid.View.VndbApplication
import com.example.ProjetAndroid.View.adapters.ScreenshotGroupAdapter
import com.example.ProjetAndroid.ViewModel.viewmodels.lazyViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VnDetailsFragment : Fragment() {

    private val args by navArgs<VnDetailsFragmentArgs>()

    private var _binding: FragmentVnDetailsBinding? = null
    private val binding: FragmentVnDetailsBinding
        get() = _binding ?: throw RuntimeException("VnDetails == null")

    private val component by lazy {
        (requireActivity().application as VndbApplication).component
    }

    private val viewModel by lazyViewModel { stateHandle ->
        component.vnItemViewModel().create(args.id, stateHandle)
    }

    private val screenshotGroupAdapter by lazy {
        ScreenshotGroupAdapter()
    }
    private val fragList by lazy {
        listOf(
            TagsFragment(),
            TagsFragment(),
            TagsFragment()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVnDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenshots.adapter = screenshotGroupAdapter
        childFragmentManager.beginTransaction()
            .replace(binding.tabsFragmentPlaceholder.id, fragList[0])
            .commit()
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                childFragmentManager.beginTransaction()
                    .replace(binding.tabsFragmentPlaceholder.id, fragList[tab?.position!!]).commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
                    binding.poster.load(state.vn.image) {
                        crossfade(true)
                        crossfade(200)
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.gif_error)
                    }

                    binding.title.text = state.vn.title
                    binding.description.text = state.vn.description
                    screenshotGroupAdapter.submitList(state.vn.screenshots)
                }
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}