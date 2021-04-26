package com.example.library.fragments.startActivityFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.activities.MainActivity
import com.example.library.enum.Status
import com.example.library.fragments.mainActivityFragments.showError
import com.example.library.modules.SessionManager
import com.example.library.viewmodels.userViewModels.LoginViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var progressBar: LinearProgressIndicator

    private val sessionManager: SessionManager by inject()

    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val currentView = inflater.inflate(R.layout.fragment_login, null)
        if (sessionManager.isUserIn()) {
            val destination = ActivityNavigator(currentView.context).createDestination()
                .setIntent(Intent(currentView.context, MainActivity::class.java))
            ActivityNavigator(currentView.context).navigate(destination, null, null, null)
        }
        registerButton = currentView.findViewById(R.id.register_to_register_button)
        loginButton = currentView.findViewById(R.id.login_login_button)
        email = currentView.findViewById(R.id.login_email_editText)
        password = currentView.findViewById(R.id.login_password_editText)
        progressBar = currentView.findViewById(R.id.progressBar_login)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), R.string.LoginSuccess, Toast.LENGTH_SHORT)
                            .show()
                        val destination =
                            ActivityNavigator(currentView.context).createDestination()
                                .setIntent(
                                    Intent(
                                        currentView.context,
                                        MainActivity::class.java
                                    )
                                )
                        ActivityNavigator(currentView.context).navigate(
                            destination,
                            null,
                            null,
                            null
                        )

                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        showError(resources.getString(R.string.noEthernet)).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
        loginButton.setOnClickListener {
            viewModel.login(email.text.toString(), password.text.toString())
        }
        registerButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.registerFragment)
        }
        return currentView
    }

}