package com.example.fu.ui.enter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentRegisterBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)

    val viewModel: RegisterViewModel by viewModels(Scopes.APP_SCOPE , Scopes.APP_ACTIVITY_SCOPE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.ok.setOnClickListener {
            viewModel.register(
                binding.login.text.toString(),
                binding.password.text.toString(),
                binding.passwordRepeat.text.toString()
                )
        }

        viewModel.loginViewState.observe(viewLifecycleOwner){
            when(it){
                is RegisterViewModel.LoginViewState.BlankViewState -> {
                    binding.ProgressBar.isVisible = false
                }
                is RegisterViewModel.LoginViewState.LoadingViewState -> {
                    binding.ProgressBar.isVisible = true
                }
                is RegisterViewModel.LoginViewState.Data -> {
                    binding.ProgressBar.isVisible = false
                    if(it.data.success){
                        Toast.makeText(context, "Регистрация прошла успешно, иди логинься", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context, "Чето не так(", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}