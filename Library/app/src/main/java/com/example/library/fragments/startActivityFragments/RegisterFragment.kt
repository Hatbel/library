package com.example.library.fragments.startActivityFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import com.example.library.R
import com.example.library.activities.MainActivity
import com.example.library.enum.Status
import com.example.library.fragments.mainActivityFragments.showError
import com.example.library.viewmodels.userViewModels.RegisterViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private lateinit var registerButton: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: LinearProgressIndicator

    private val viewModel by viewModel<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currentView = inflater.inflate(R.layout.fragment_register, null)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), R.string.RegisterSuccess, Toast.LENGTH_SHORT)
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
        registerButton = currentView.findViewById(R.id.register_button)
        email = currentView.findViewById(R.id.email_editText)
        password = currentView.findViewById(R.id.password_editText)
        progressBar = currentView.findViewById(R.id.progressBar_register)
        registerButton.setOnClickListener {
            viewModel.createUser(email.text.toString(), password.text.toString())
        }
        return currentView
    }
}