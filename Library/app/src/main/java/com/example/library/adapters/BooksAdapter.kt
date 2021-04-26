package com.example.library.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.library.R
import com.example.library.beans.daoModels.DaoBook
import com.example.library.databinding.BooksItemLayoutBinding
import com.example.library.interfaces.CellClickListener


class BooksAdapter(
    private var books: List<DaoBook>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<BooksAdapter.DataViewHolder>() {
    class DataViewHolder(_binding: BooksItemLayoutBinding) : RecyclerView.ViewHolder(_binding.root) {

        private var binding: BooksItemLayoutBinding? = _binding

        fun bind(book: DaoBook) {
            binding?.book = book
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding: BooksItemLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.books_item_layout,
                parent,
                false
            )
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(books[position])
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(books[position].id)
        }

    }

    fun addBooks(books: MutableList<DaoBook>) {
        this.books = books
        notifyDataSetChanged()
    }
}