package com.example.ProjetAndroid.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ProjetAndroid.databinding.FragmentTagsBinding
import com.example.ProjetAndroid.presentation.adapters.TagListAdapter
import com.example.ProjetAndroid.presentation.viewmodels.VnItemViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TagsFragment : Fragment() {

    private var _binding: FragmentTagsBinding? = null
    private val binding: FragmentTagsBinding
        get() = _binding ?: throw RuntimeException("TagsFragment == null")

    private val tagListAdapter by lazy {
        TagListAdapter()
    }

    private val viewModel: VnItemViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tagList.adapter = tagListAdapter
        setupChips()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
                    tagListAdapter.submitList(state.vn.tags)
                    binding.tagContentChip.isChecked = state.content
                    binding.tagTechnicalChip.isChecked = state.technical

                }
            }
        }
    }

    private fun setupChips() {
        binding.tagContentChip.setOnClickListener {
            viewModel.changeContent()
        }
        binding.tagTechnicalChip.setOnClickListener {
            viewModel.changeTechnical()
        }


        }
    }

