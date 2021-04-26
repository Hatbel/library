package com.example.library.fragments.mainActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.enum.Status
import com.example.library.viewmodels.booksViewModels.CreateBookViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CreateBookFragment : Fragment() {

    private lateinit var createBook: Button
    private lateinit var bookName: EditText

    private val viewModel by viewModel<CreateBookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val currentView = inflater.inflate(R.layout.fragment_create_book, null)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), R.string.bookCreated, Toast.LENGTH_SHORT).show()
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
        createBook = currentView.findViewById(R.id.createBook_createBookView_button)
        bookName = currentView.findViewById(R.id.book_name_editText)
        createBook.setOnClickListener {
            viewModel.createBook(bookName.text.toString())
        }
        return currentView
    }

}