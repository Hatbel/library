package com.example.library.fragments.startActivityFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.activities.MainActivity
import com.example.library.databinding.FragmentLoginBinding
import com.example.library.databinding.FragmentRegisterBinding
import com.example.library.enum.Status
import com.example.library.fragments.mainActivityFragments.showError
import com.example.library.viewmodels.userViewModels.RegisterViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private val viewModel by viewModel<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.viewModel = viewModel
        binding.registerButton.setOnClickListener { viewModel.createUser() }
        viewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), R.string.RegisterSuccess, Toast.LENGTH_SHORT)
                            .show()
                        val destination =
                            ActivityNavigator(requireContext()).createDestination()
                                .setIntent(
                                    Intent(
                                        requireContext(),
                                        MainActivity::class.java
                                    )
                                )
                        ActivityNavigator(requireContext()).navigate(
                            destination,
                            null,
                            null,
                            null
                        )
                    }
                    Status.ERROR -> {
                        Log.e("asdasdasd", it.message)
                        showError(resources.getString(R.string.noEthernet)).show()
                    }
                    else -> {
                        showError(resources.getString(R.string.unknownError)).show()
                    }
                }
            }
        })
        return binding.root
    }
}