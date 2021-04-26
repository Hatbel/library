package com.example.library.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.library.R
import com.example.library.beans.daoModels.DaoBook
import com.example.library.interfaces.CellClickListener

class BooksAdapter(
    private var books: List<DaoBook>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<BooksAdapter.DataViewHolder>() {
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bookName: TextView = itemView.findViewById(R.id.textViewBookName)
        private val bookStatus: TextView = itemView.findViewById(R.id.textViewBookStatus)
        fun bind(book: DaoBook) {
            bookName.text = book.name
            bookStatus.text = book.status
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.books_item_layout, parent, false)
        )

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