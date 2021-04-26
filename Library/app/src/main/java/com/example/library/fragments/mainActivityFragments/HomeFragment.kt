package com.example.library.fragments.mainActivityFragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
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
import com.example.library.databinding.FragmentProfileBinding
import com.example.library.interfaces.CellClickListener
import com.example.library.server.BookScreenState
import com.example.library.viewmodels.booksViewModels.BooksViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), CellClickListener {

    private lateinit var adapter: BooksAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentHomeBinding

    val viewModel by viewModel<BooksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        recyclerView = binding.recyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                    DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                    )
            )
        }
        adapter = BooksAdapter(arrayListOf(), this)
        recyclerView.adapter = adapter
        observeState()
        addScrollerListener()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.books_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        observeState()
        when (item.itemId) {
            R.id.action_allBooks -> {
                viewModel.clear()
                recyclerView.clearOnScrollListeners()
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        viewModel.checkPosition((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition())
                    }
                })
                return true
            }

            R.id.action_availebleBooks -> {
                viewModel.clear()
                recyclerView.clearOnScrollListeners()
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        viewModel.getAvailableBooks((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition())
                    }
                })
                return true
            }
            R.id.action_expiredBooks -> {
                viewModel.clear()
                recyclerView.clearOnScrollListeners()
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        viewModel.getExpiredBooks((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition())
                    }
                })
                return true
            }
            R.id.action_userReadBooks -> {
                recyclerView.clearOnScrollListeners()
                viewModel.getUserReadBooks()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    private fun addScrollerListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.checkPosition((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition())
            }
        })
    }

    override fun onCellClickListener(position: Int) {
        val navController = NavHostFragment.findNavController(this)
        viewModel.savePosition(position)
        navController.navigate(R.id.showBookFragment)
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

fun Fragment.showError(message: String): AlertDialog =
        AlertDialog.Builder(requireContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", null)
                .create()
