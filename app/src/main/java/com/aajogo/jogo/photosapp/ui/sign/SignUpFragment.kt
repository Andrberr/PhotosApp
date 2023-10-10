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
import com.aajogo.jogo.photosapp.databinding.FragmentSignUpBinding
import com.aajogo.jogo.photosapp.domain.models.UserData
import com.aajogo.jogo.photosapp.ui.sign.viewpager.PagerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<SignViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initObserver()
    }

    private fun initViews() {
        with(binding) {
            signUpButton.setOnClickListener {
                checkLoginAndPassword(
                    inputLogin.text.toString(),
                    inputPassword.text.toString(),
                    confirmPassword.text.toString()
                )
            }
        }
    }

    private fun initObserver() {
        viewModel.isSignUp.observe(viewLifecycleOwner) { isSignUp ->
            if (isSignUp) {
                navigateToPhotos()
            } else {
                showText(getString(R.string.error_text))
            }
        }
    }

    private fun checkLoginAndPassword(login: String, password: String, confirmPassword: String) {
        if (isLoginValid(login)) {
            if (isPasswordValid(password)) {
                if (password == confirmPassword) {
                    viewModel.signUp(UserData(login, password))
                } else showText(getString(R.string.passwords_error))
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        if (password.length != PASSWORD_LENGTH) {
            binding.inputPassword.error = getString(R.string.password_length_error)
            return false
        }
        val hasDigit = password.any { it.isDigit() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val isValid = hasDigit && hasUpperCase && hasLowerCase
        if (!isValid) {
            binding.inputPassword.error = getString(R.string.password_error)
            return false
        }
        return true
    }

    private fun isLoginValid(login: String): Boolean {
        if (login.length < LOGIN_MIN_LENGTH) {
            binding.inputLogin.error = getString(R.string.login_length_error)
            return false
        }
        val matcher = loginPattern.matcher(login)
        val isValid = matcher.matches()
        if (!isValid) {
            binding.inputLogin.error = getString(R.string.login_error)
            return false
        }
        return true
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

    companion object {
        private const val PASSWORD_LENGTH = 10
        private const val LOGIN_MIN_LENGTH = 4
        private val loginPattern = Pattern.compile("[a-z\\d_\\-.@]+")
    }
}