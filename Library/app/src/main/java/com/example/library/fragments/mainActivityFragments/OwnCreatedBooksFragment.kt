package com.example.library.fragments.mainActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.library.R
import com.example.library.adapters.BooksAdapter
import com.example.library.beans.daoModels.DaoBook
import com.example.library.interfaces.CellClickListener
import com.example.library.server.BookScreenState
import com.example.library.viewmodels.booksViewModels.OwnCreatedBooksViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel

class OwnCreatedBooksFragment : Fragment(), CellClickListener {
    private lateinit var adapter: BooksAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: LinearProgressIndicator

    private val viewModel by viewModel<OwnCreatedBooksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_own_created_books, container, false)
        recyclerView = view.findViewById<View>(R.id.own_created_books_recyclerView) as RecyclerView
        progressBar = view.findViewById<View>(R.id.progressBar_ownBooks) as LinearProgressIndicator
        setupUI()
        return view
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BooksAdapter(arrayListOf(), this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
        observeState()
    }

    override fun onCellClickListener(position: Int) {
        viewModel.savePosition(position)
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.addedBookFragment)
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BookScreenState.Idle -> {
                    progressBar.visibility = View.VISIBLE
                }
                is BookScreenState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is BookScreenState.Books -> {
                    progressBar.visibility = View.GONE
                    adapter.addBooks(it.books as MutableList<DaoBook>)
                }
                is BookScreenState.Error -> {
                    progressBar.visibility = View.GONE
                    showError(resources.getString(R.string.unknownError)).show()
                }
                else -> {
                    showError(resources.getString(R.string.unknownError)).show()

                }
            }
        })
    }
}