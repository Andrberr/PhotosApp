package com.aajogo.jogo.photosapp.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aajogo.jogo.photosapp.databinding.FragmentSplashBinding
import com.aajogo.jogo.photosapp.ui.sign.SignViewModel

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<SignViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.login.observe(viewLifecycleOwner) { login ->
            val action =
                if (login.isEmpty()) SplashFragmentDirections.actionSplashFragmentToPagerFragment()
                else SplashFragmentDirections.actionSplashFragmentToPhotosFragment()
            findNavController().navigate(action)
        }
        viewModel.getUserLogin()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}