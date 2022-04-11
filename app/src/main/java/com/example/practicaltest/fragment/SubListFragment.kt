package com.example.practicaltest.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.navigation.fragment.navArgs
import com.example.practicaltest.activity.MainActivity
import com.example.practicaltest.responseModel.SublistData
import com.example.practicaltest.utils.Prefs
import com.example.practicaltest.utils.Result
import com.example.practicaltest.utils.showSnackbar
import com.example.practicaltest.viewModel.SubListViewModel
import com.example.practicaltest.adapter.ExpandableListAdapter
import com.example.practicaltest.databinding.SubListFragmentBinding
import com.example.practicaltest.viewModelProvider.ViewModelProvider
import androidx.core.content.ContextCompat
import com.example.practicaltest.databinding.CustomdialogBinding


class SubListFragment : Fragment() {

    private lateinit var viewModel: SubListViewModel
    lateinit var binding: SubListFragmentBinding
    lateinit var adapter: ExpandableListAdapter
    val args: SubListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubListFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider().create(SubListViewModel::class.java)

        Prefs.authAPI = args.apikey

        viewModel.callSublistAPI()

        viewModel.responseModel.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Loading -> {
                    (activity as MainActivity).showLoadingOverlay()
                }

                is Result.Error -> {
                    (activity as MainActivity).hideLoadingOverlay()
                    (activity as MainActivity).showSnackbar(
                        it.msg ?: "Something went wrong"
                    )
                }

                is Result.Success -> {
                    //200 Ok response of SubList API
                    // filtered data from response as per need
                    (activity as MainActivity).hideLoadingOverlay()
                    val hashmap = HashMap<String, ArrayList<SublistData>>() // it will store all the menu and it's and all name
                    val header = ArrayList<String>() // it will store all the menu for expandable list view purpose
                    with(it.responseData?.data!!) {
                        for (i in indices) {
                            if (!hashmap.containsKey(this.get(i).categoria.nombremenu)) {
                                header.add(this.get(i).categoria.nombremenu)
                                val listof = ArrayList<SublistData>()
                                val list = SublistData(this.get(i).nombre).apply {
                                    precioSugerido = (this@with.get(i).precioSugerido).toFloat()
                                    quantity = 1
                                }
                                listof.add(list)
                                hashmap.put(this.get(i).categoria.nombremenu, listof)
                            } else {
                                val listof = hashmap.get(this.get(i).categoria.nombremenu)
                                val list = SublistData(this.get(i).nombre).apply {
                                    precioSugerido = (this@with.get(i).precioSugerido).toFloat()
                                    quantity = 1
                                }
                                listof?.add(list)
                                hashmap.put(this.get(i).categoria.nombremenu, listof!!)
                            }
                        }
                        Log.e("hashmap", "" + hashmap)
                        //expandable list view Adapter
                        adapter = ExpandableListAdapter(requireContext(), header, hashmap)
                        binding.expandedList.setAdapter(adapter)

                        binding.expandedList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

                            //show alertDialog
                            showAlertDialog(
                                hashmap.get(header.get(groupPosition))?.get(childPosition),
                                groupPosition,
                                header,
                                hashmap,
                                childPosition
                            )
                            false
                        }
                    }
                }
            }

        })
    }

    private fun showAlertDialog(
        list: SublistData?,
        groupPosition: Int,
        header: ArrayList<String>,
        hashmap: HashMap<String, ArrayList<SublistData>>,
        childPosition: Int
    ) {
        var quantityCount = list!!.quantity
        val orignalPrice = list.precioSugerido
        var price = list.precioSugerido
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val binding = CustomdialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.nameTxt.text = list.nombre
        binding.priceTxt.text = list.precioSugerido.toString()
        binding.quantity.setText(quantityCount.toString())

        binding.plusBtn.setOnClickListener {
            quantityCount++
            binding.quantity.setText(quantityCount.toString())
            price += orignalPrice
            binding.priceTxt.text = price.toString()
        }
        binding.minusBtn.setOnClickListener {
            if (quantityCount != 0) {
                quantityCount--
                binding.quantity.setText(quantityCount.toString())
                price -= orignalPrice
                binding.priceTxt.text = price.toString()
            }
        }

        binding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding.confirmBtn.setOnClickListener {
            val newList = SublistData(list.nombre).apply {
                quantity = quantityCount
                precioSugerido = price
            }

            //update existing list to reflect
            val mainList = hashmap.get(header.get(groupPosition))!!
            mainList.set(childPosition, newList)
            hashmap.put(header.get(groupPosition), mainList)
            adapter = ExpandableListAdapter(requireContext(), header, hashmap)
            this.binding.expandedList.setAdapter(adapter)
            dialog.dismiss()

        }

        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                android.R.color.transparent
            )
        )
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }
}