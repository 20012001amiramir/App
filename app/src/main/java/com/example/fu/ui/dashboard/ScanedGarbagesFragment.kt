package com.example.fu.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentDashboardBinding
import com.example.fu.di.AppModule
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.di.objectScopeName
import com.example.fu.ui.common.extention.swapItems
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import toothpick.Toothpick

class ScanedGarbagesFragment : Fragment(R.layout.fragment_dashboard){

    private val binding by viewBinding(FragmentDashboardBinding::bind)

//    private val delegates = ListDelegationAdapter(
//        typeGarbageListDelegate()
//    )

    private val scanedGarbagesViewModel: ScanedGarbagesViewModel by viewModels(Scopes.APP_SCOPE , Scopes.APP_ACTIVITY_SCOPE)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toothpick
            .openScope(Scopes.objectScopeName)
            .installModules(context?.let { AppModule(context = it) })

        binding.recyclerview.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            //adapter = delegates
        }
        scanedGarbagesViewModel.loadGarbagesInfo()

//        binding.floatButton.setOnClickListener {
//            findNavController().navigateSafe(R.id.navigation_dashboard, R.id.action_navigation_dashboard_to_addGarbageDialogFragment)
//        }

        scanedGarbagesViewModel.garbageScannedGarbagesState.observe(viewLifecycleOwner){
            when(it){
                is GarbageViewState.BlankViewState -> {
                    binding.proggressBar.isVisible = false
                    binding.empty.isVisible = true
                }
                is GarbageViewState.LoadingViewState -> {
                    binding.proggressBar.isVisible = true
                    binding.empty.isVisible = false
                }
                is GarbageViewState.Data -> {
                    binding.proggressBar.isVisible = false
                    binding.empty.isVisible = false

                    //delegates.swapItems(it.data.data)

                }
                else -> {}
            }
        }

    }
}