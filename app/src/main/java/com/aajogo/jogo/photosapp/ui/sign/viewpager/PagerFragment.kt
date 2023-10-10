package com.aajogo.jogo.photosapp.ui.sign.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aajogo.jogo.photosapp.databinding.ViewPagerBinding
import com.aajogo.jogo.photosapp.ui.sign.SignInFragment
import com.aajogo.jogo.photosapp.ui.sign.SignUpFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagerFragment : Fragment() {

    private var _binding: ViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = PagerAdapter(this)
        adapter.apply {
            addFragment(SignInFragment(), "Login")
            addFragment(SignUpFragment(), "Register")
        }

        val viewPager = binding.viewpager
        viewPager.apply {
            this.adapter = adapter
            currentItem = 0
        }

        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }
}