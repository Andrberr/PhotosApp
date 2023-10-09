package com.aajogo.jogo.photosapp.ui.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aajogo.jogo.photosapp.R
import com.aajogo.jogo.photosapp.databinding.FragmentSignInBinding
import com.aajogo.jogo.photosapp.domain.models.UserData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<SignViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initObserver()
    }

    private fun initViews() {
        binding.loginButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        with(binding) {
            val login = inputLogin.text.toString()
            val password = inputPassword.text.toString()
            if (login.isNotEmpty()) {
                if (password.isNotEmpty()) {
                    viewModel.signIn(UserData(login, password))
                } else binding.inputPassword.error = getString(R.string.empty_password)
            } else binding.inputLogin.error = getString(R.string.empty_login)
        }
    }

    private fun initObserver() {
        viewModel.isSignIn.observe(viewLifecycleOwner) { isSignIn ->
            if (isSignIn) {
                navigateToPhotos()
            } else {
                showText(getString(R.string.error_text))
            }
        }
    }

    private fun navigateToPhotos() {
        val action = PagerFragmentDirections.actionPagerFragmentToPhotosFragment()
        findNavController().navigate(action)
    }

    private fun showText(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}