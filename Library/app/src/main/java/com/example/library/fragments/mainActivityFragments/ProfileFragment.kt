package com.example.library.fragments.mainActivityFragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.library.R
import com.example.library.activities.StartActivity
import com.example.library.databinding.FragmentProfileBinding
import com.example.library.enum.Status
import com.example.library.viewmodels.userViewModels.ProfileViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
    private lateinit var navController: NavController

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = NavHostFragment.findNavController(this)
        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.user = it.data
                    }
                    Status.ERROR -> {
                        showError(resources.getString(R.string.unknownError)).show()
                    }
                    else -> showError(resources.getString(R.string.unknownError)).show()
                }
            }
        })
        viewModel.getUser()
        binding.booksProfileButton.setOnClickListener { navController.navigate(R.id.fragmentBooks) }
        binding.createBookButton.setOnClickListener { navController.navigate(R.id.createBookFragment) }
        binding.mybooksProfileButton.setOnClickListener { navController.navigate(R.id.myBooksFragment) }
        return binding.root
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