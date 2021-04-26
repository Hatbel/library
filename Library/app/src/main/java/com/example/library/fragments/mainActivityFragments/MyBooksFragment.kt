package com.example.library.fragments.mainActivityFragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.databinding.FragmentMyBooksBinding
import com.example.library.databinding.FragmentShowBookBinding
import com.example.library.enum.Status
import com.example.library.server.BookScreenState
import com.example.library.server.repositories.toyyyymmddString
import com.example.library.viewmodels.booksViewModels.ReturnBookViewModel
import com.example.library.viewmodels.booksViewModels.ShowMyBooksViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel


class MyBooksFragment : Fragment() {
    private lateinit var bookName: String

    private val showBookViewModel by viewModel<ShowMyBooksViewModel>()
    private val returnBookViewModel by viewModel<ReturnBookViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, bookName)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_item, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMyBooksBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_books, container, false)
        binding.returnBookButton.setOnClickListener { returnBookViewModel.returnBook() }
        showBookViewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BookScreenState.Book -> {
                    bookName = it.book.name
                    binding.book = it.book
                }
                is BookScreenState.Error -> {
                    showError(resources.getString(R.string.notBook)).show()
                }
                else -> {
                    showError(resources.getString(R.string.unknownError)).show()
                }
            }
        })
        returnBookViewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), R.string.returnedBook, Toast.LENGTH_SHORT).show()
                        val navController = NavHostFragment.findNavController(this)
                        navController.navigate(R.id.fragmentBooks)
                    }
                    Status.ERROR -> {
                        showError(resources.getString(R.string.unknownError)).show()
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