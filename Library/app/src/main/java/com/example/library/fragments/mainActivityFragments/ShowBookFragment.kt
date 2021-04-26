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
import com.example.library.viewmodels.booksViewModels.ReserveBookViewModel
import com.example.library.viewmodels.booksViewModels.ShowBookViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel


class ShowBookFragment : Fragment() {
    private lateinit var bookName: TextView
    private lateinit var bookStatus: TextView
    private lateinit var bookDeadLine: TextView
    private lateinit var reserveButton: Button
    private lateinit var progressBar: LinearProgressIndicator

    private val showBookViewModel by viewModel<ShowBookViewModel>()
    private val reserveBookViewModel by viewModel<ReserveBookViewModel>()

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
        val view: View = inflater.inflate(R.layout.fragment_show_book, container, false)
        bookName = view.findViewById(R.id.bookName_showBook_textView)
        bookStatus = view.findViewById(R.id.status_showBook_textView)
        bookDeadLine = view.findViewById(R.id.deadLine_showBook_textView)
        reserveButton = view.findViewById(R.id.reserveBook_Button)
        progressBar = view.findViewById(R.id.progressBar_showBooks)
        reserveButton.setOnClickListener {
            reserveBookViewModel.reserveBook()
        }
        observeState()
        return view
    }

    private fun observeState() {

        showBookViewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BookScreenState.HideButton -> {
                    bookName.visibility = View.VISIBLE
                    bookStatus.visibility = View.VISIBLE
                    bookDeadLine.visibility = View.VISIBLE
                    bookName.text = it.book.name
                    bookStatus.text = it.book.status
                    bookDeadLine.text = it.book.dead_line.toyyyymmddString()
                    reserveButton.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
                is BookScreenState.HideDeadLine -> {
                    bookName.visibility = View.VISIBLE
                    bookStatus.visibility = View.VISIBLE
                    bookDeadLine.visibility = View.GONE
                    bookName.text = it.book.name
                    bookStatus.text = it.book.status
                    reserveButton.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
                is BookScreenState.Loading -> {
                    bookName.visibility = View.GONE
                    bookStatus.visibility = View.GONE
                    bookDeadLine.visibility = View.GONE
                    reserveButton.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                is BookScreenState.Book -> {
                    bookName.visibility = View.VISIBLE
                    bookStatus.visibility = View.VISIBLE
                    bookDeadLine.visibility = View.GONE
                    reserveButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
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