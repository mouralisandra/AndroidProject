package com.example.ProjetAndroid.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ProjetAndroid.databinding.FragmentScreenshotPagerBinding
import com.example.ProjetAndroid.presentation.adapters.ViewPagerAdapter

class ScreenshotPagerFragment : Fragment() {

    private val args by navArgs<ScreenshotPagerFragmentArgs>()
    private var _binding: FragmentScreenshotPagerBinding? = null
    private val binding: FragmentScreenshotPagerBinding
        get() = _binding ?: throw RuntimeException("ScreenshotPagerFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreenshotPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(args.urls)
        binding.screenshotPager.adapter = adapter
        binding.screenshotPager.setCurrentItem(args.selectedImagePosition, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}