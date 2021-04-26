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
import com.example.library.databinding.FragmentAddedBookBinding
import com.example.library.enum.Status
import com.example.library.server.BookScreenState
import com.example.library.viewmodels.booksViewModels.AddedBookViewModel
import com.example.library.viewmodels.booksViewModels.ReserveBookViewModel
import com.example.library.viewmodels.booksViewModels.ShowBookViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class AddedBookFragment : Fragment() {

    private val showBookViewModel by viewModel<ShowBookViewModel>()
    private val addedBookViewModel by viewModel<AddedBookViewModel>()
    private val reserveBookViewModel by viewModel<ReserveBookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddedBookBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_added_book, container, false)
        binding.reserveBookButtonAddedBook.setOnClickListener { reserveBookViewModel.reserveBook() }
        binding.returnAddedBook.setOnClickListener { addedBookViewModel.returnBook() }
        observeState(binding)
        return binding.root
    }

    private fun observeState(binding: FragmentAddedBookBinding) {
        showBookViewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BookScreenState.Book -> {
                    binding.book = it.book
                }
                is BookScreenState.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT)
                        .show()
                }
                else -> showError(resources.getString(R.string.unknownError)).show()
            }
        })
        addedBookViewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), R.string.reserveBook, Toast.LENGTH_SHORT)
                            .show()
                        val navController = NavHostFragment.findNavController(this)
                        navController.navigate(R.id.ownCreatedBooksFragment)
                    }
                    Status.ERROR -> {
                        showError(resources.getString(R.string.notReserve)).show()

                    }
                    else -> {
                        showError(resources.getString(R.string.unknownError)).show()
                    }

                }
            }
        })
        reserveBookViewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), R.string.reserveBook, Toast.LENGTH_SHORT)
                            .show()
                        val navController = NavHostFragment.findNavController(this)
                        navController.navigate(R.id.myBooksFragment)
                    }
                    Status.ERROR -> {
                        showError(resources.getString(R.string.noReserve)).show()
                    }
                    else -> {
                        showError(resources.getString(R.string.unknownError)).show()
                    }

                }
            }
        })

    }

}

