package com.example.practicaltest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practicaltest.responseModel.Franquicia
import com.example.practicaltest.databinding.MainlistitemBinding

class MainListAdapter :
    ListAdapter<Franquicia, MainListAdapter.ViewHolder>(POST_COMPARATOR) {

    inner class ViewHolder(val binding: MainlistitemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MainlistitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            getItem(position).let {
                title.text = it.negocio
                root.setOnClickListener { _ ->
                    onItemClick?.invoke(it.APIKEY)
                }
            }
        }
    }

    var onItemClick: ((String) -> Unit)? = null

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Franquicia>() {
            override fun areItemsTheSame(oldItem: Franquicia, newItem: Franquicia) =
                oldItem.APIKEY == newItem.APIKEY

            override fun areContentsTheSame(oldItem: Franquicia, newItem: Franquicia) =
                oldItem == newItem
        }
    }
}