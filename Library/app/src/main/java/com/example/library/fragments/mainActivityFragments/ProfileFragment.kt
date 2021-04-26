package com.example.library.fragments.mainActivityFragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.activities.StartActivity
import com.example.library.enum.Status
import com.example.library.viewmodels.userViewModels.ProfileViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
    private lateinit var myBooksButton: Button
    private lateinit var createBookButton: Button
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var booksButton: Button
    private lateinit var email: TextView

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val currentView: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val navController = NavHostFragment.findNavController(this)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        booksButton.visibility = View.VISIBLE
                        myBooksButton.visibility = View.VISIBLE
                        createBookButton.visibility = View.VISIBLE
                        email.visibility = View.VISIBLE
                        email.text = it.data?.mail.toString()
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        showError(resources.getString(R.string.unknownError)).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        booksButton.visibility = View.GONE
                        myBooksButton.visibility = View.GONE
                        createBookButton.visibility = View.GONE
                        email.visibility = View.GONE
                    }
                }
            }
        })
        viewModel.getUser()

        booksButton = currentView.findViewById(R.id.books_profile_button)
        myBooksButton = currentView.findViewById(R.id.mybooks_profile_button)
        createBookButton = currentView.findViewById(R.id.create_book_button)
        progressBar = currentView.findViewById(R.id.progressBar_profile)
        booksButton.setOnClickListener { navController.navigate(R.id.fragmentBooks) }
        myBooksButton.setOnClickListener { navController.navigate(R.id.myBooksFragment) }
        createBookButton.setOnClickListener { navController.navigate(R.id.createBookFragment) }
        email = currentView.findViewById(R.id.profile_login_textView)
        return currentView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logOut) {
            viewModel.logout()
            val destination = ActivityNavigator(requireContext()).createDestination()
                .setIntent(Intent(requireContext(), StartActivity::class.java))
            requireContext().let {
                destination.let { destination ->
                    ActivityNavigator(it).navigate(
                        destination,
                        null,
                        null,
                        null
                    )
                }
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_item, menu)
    }
}