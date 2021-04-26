package com.example.library.fragments.mainActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.databinding.FragmentCreateBookBinding
import com.example.library.enum.Status
import com.example.library.viewmodels.booksViewModels.CreateBookViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CreateBookFragment : Fragment() {

    private val viewModel by viewModel<CreateBookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentCreateBookBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_book, container, false)
        binding.viewModel = viewModel
        binding.createBookCreateBookViewButton.setOnClickListener { viewModel.createBook() }
        viewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), R.string.bookCreated, Toast.LENGTH_SHORT)
                            .show()
                        val navController = NavHostFragment.findNavController(this)
                        navController.navigate(R.id.fragmentBooks)
                    }
                    Status.ERROR -> {
                        showError(resources.getString(R.string.notBook)).show()
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