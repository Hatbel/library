package com.example.library.fragments.mainActivityFragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.enum.Status
import com.example.library.server.BookScreenState
import com.example.library.server.repositories.toyyyymmddString
import com.example.library.viewmodels.booksViewModels.ReturnBookViewModel
import com.example.library.viewmodels.booksViewModels.ShowMyBooksViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel


class MyBooksFragment : Fragment() {
    private lateinit var bookName: TextView
    private lateinit var bookStatus: TextView
    private lateinit var bookDeadLine: TextView
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var returnButton: Button

    private val showBookViewModel by viewModel<ShowMyBooksViewModel>()
    private val returnBookViewModel by viewModel<ReturnBookViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, bookName.text)
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
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_my_books, container, false)
        bookName = view.findViewById(R.id.bookName_reservedBook_textView)
        bookStatus = view.findViewById(R.id.status_reservedBook_textView)
        progressBar = view.findViewById(R.id.progressBar_myBook)
        bookDeadLine = view.findViewById(R.id.deadLine_reservedBook_textView)
        returnButton = view.findViewById(R.id.returnBook_Button)
        returnButton.setOnClickListener {
            returnBookViewModel.returnBook()
        }
        observeState()
        return view
    }

    private fun observeState() {
        showBookViewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {

                is BookScreenState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    bookName.visibility = View.GONE
                    bookStatus.visibility = View.GONE
                    bookDeadLine.visibility = View.GONE
                    returnButton.visibility = View.GONE
                }
                is BookScreenState.HideButton -> {
                    progressBar.visibility = View.GONE
                    bookName.visibility = View.VISIBLE
                    bookStatus.visibility = View.VISIBLE
                    bookDeadLine.visibility = View.VISIBLE
                    returnButton.visibility = View.VISIBLE
                    bookName.text = it.book.name
                    bookStatus.text = it.book.status
                    bookDeadLine.text = it.book.dead_line.toyyyymmddString()
                }
                is BookScreenState.Book -> {
                    progressBar.visibility = View.GONE
                    bookName.visibility = View.VISIBLE
                    bookStatus.visibility = View.VISIBLE
                    bookDeadLine.visibility = View.GONE
                    returnButton.visibility = View.VISIBLE
                    bookName.text = it.book.name
                    bookStatus.text = it.book.status
                }
                is BookScreenState.Error -> {
                    progressBar.visibility = View.GONE
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

    }
}