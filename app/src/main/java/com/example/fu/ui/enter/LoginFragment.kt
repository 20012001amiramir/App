package com.example.fu.ui.enter

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentLoginBinding
import com.example.fu.databinding.FragmentRegisterBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.util.navigateSafe

class LoginFragment : Fragment(R.layout.fragment_login) {


    private val binding by viewBinding(FragmentLoginBinding::bind)

    val viewModel: LoginViewModel by viewModels(Scopes.APP_SCOPE , Scopes.APP_ACTIVITY_SCOPE)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ok.setOnClickListener {
            viewModel.authorize(
                binding.login.text.toString(),
                binding.password.text.toString()
            )
        }

        viewModel.loginViewState.observe(viewLifecycleOwner){
            when(it){
                is LoginViewModel.LoginViewState.BlankViewState -> {
                    binding.ProgressBar.isVisible = false
                }
                is LoginViewModel.LoginViewState.LoadingViewState -> {
                    binding.ProgressBar.isVisible = true
                }
                is LoginViewModel.LoginViewState.Data -> {
                    binding.ProgressBar.isVisible = false
                    if(it.data.success){
                        viewModel.saveToken(it.data.data?.accessToken?:"", it.data.data?.refreshToken?:"")
                        //findNavController().navigateSafe(R.id.loginFragment, R.id.action_loginFragment_to_navigation_home)
                    }
                    else{
                        Toast.makeText(context, "Чето не так(", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}