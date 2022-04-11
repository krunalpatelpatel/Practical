package com.example.practicaltest.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicaltest.activity.MainActivity
import com.example.practicaltest.viewModel.MainListViewModel
import com.example.practicaltest.adapter.MainListAdapter
import com.example.practicaltest.databinding.MainListFragmentBinding
import com.example.practicaltest.viewModelProvider.ViewModelProvider
import com.example.practicaltest.utils.Result
import com.example.practicaltest.utils.showSnackbar

class MainListFragment : Fragment() {

    private lateinit var viewModel: MainListViewModel
    lateinit var binding: MainListFragmentBinding
    lateinit var mainListAdapter: MainListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainListFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider().create(MainListViewModel::class.java)

        // initialization of recyclerview
        binding.mainList.layoutManager = LinearLayoutManager(requireActivity())
        mainListAdapter = MainListAdapter()
        binding.mainList.adapter = mainListAdapter


        // observer of mainList API response data class
        viewModel.responseModel.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Loading -> {
                    //Loading will visible
                    (activity as MainActivity).showLoadingOverlay()
                }

                is Result.Error -> {
                    // API failure
                    (activity as MainActivity).hideLoadingOverlay()
                    (activity as MainActivity).showSnackbar(
                        it.msg ?: "Something went wrong"
                    )
                }

                is Result.Success -> {
                    // 200 ok API response
                    (activity as MainActivity).hideLoadingOverlay()
                    mainListAdapter.submitList(it.responseData!!.franquicias)
                }
            }

        })

        mainListAdapter.onItemClick = { apikey ->
            // Pass API key in subListFragment
            val directions = MainListFragmentDirections.actionNavigationHomeToSubListHome(apikey)
            findNavController().navigate(directions)
        }
    }
}