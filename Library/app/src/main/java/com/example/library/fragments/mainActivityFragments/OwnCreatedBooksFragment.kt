package com.example.library.fragments.mainActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.library.R
import com.example.library.adapters.BooksAdapter
import com.example.library.beans.daoModels.DaoBook
import com.example.library.databinding.FragmentHomeBinding
import com.example.library.databinding.FragmentOwnCreatedBooksBinding
import com.example.library.interfaces.CellClickListener
import com.example.library.server.BookScreenState
import com.example.library.viewmodels.booksViewModels.OwnCreatedBooksViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel

class OwnCreatedBooksFragment : Fragment(), CellClickListener {
    private lateinit var adapter: BooksAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentOwnCreatedBooksBinding

    private val viewModel by viewModel<OwnCreatedBooksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_own_created_books, container, false)
        recyclerView = binding.ownCreatedBooksRecyclerView
        setupUI()
        return binding.root
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
                is BookScreenState.Books -> {
                    adapter.addBooks(it.books as MutableList<DaoBook>)
                }
                is BookScreenState.Error -> {
                    showError(resources.getString(R.string.unknownError)).show()
                }
                else -> {
                    showError(resources.getString(R.string.unknownError)).show()

                }
            }
        })
    }
}