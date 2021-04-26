package com.example.library.fragments.startActivityFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.activities.MainActivity
import com.example.library.databinding.FragmentLoginBinding
import com.example.library.enum.Status
import com.example.library.fragments.mainActivityFragments.showError
import com.example.library.modules.SessionManager
import com.example.library.viewmodels.userViewModels.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private val sessionManager: SessionManager by inject()

    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        if (sessionManager.isUserIn()) {
            val destination = ActivityNavigator(requireContext()).createDestination()
                .setIntent(Intent(requireContext(), MainActivity::class.java))
            ActivityNavigator(requireContext()).navigate(destination, null, null, null)
        }
        binding.viewModel = viewModel
        binding.loginLoginButton.setOnClickListener { viewModel.login() }
        binding.registerToRegisterButton.setOnClickListener { NavHostFragment.findNavController(this).navigate(
            R.id.registerFragment
        ) }
        viewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), R.string.LoginSuccess, Toast.LENGTH_SHORT)
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