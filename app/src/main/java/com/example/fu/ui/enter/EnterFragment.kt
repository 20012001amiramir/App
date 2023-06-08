package com.example.fu.ui.enter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentEnterBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels

class EnterFragment : Fragment(R.layout.fragment_enter) {

    private val binding by viewBinding(FragmentEnterBinding::bind)

    private val enterViewModel:
            EnterViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enterViewModel.isOnBoardingFinished) {
            findNavController().navigate(R.id.action_enterFragment_to_navigationHome)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnStart.setOnClickListener{
                enterViewModel.saveOnBoardingState(true)
                findNavController().navigate(R.id.action_enterFragment_to_navigationHome)
            }
        }
    }

}