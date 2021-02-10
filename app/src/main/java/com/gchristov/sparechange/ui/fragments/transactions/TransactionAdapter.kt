package com.gchristov.sparechange.ui.fragments.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gchristov.sparechange.databinding.ListItemTransactionBinding
import com.gchristov.sparechange.repository.model.FeedItem
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mDateFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    private val mListItems = ArrayList<FeedItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(ListItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val viewHolder = holder as ViewHolder
        viewHolder.bind(mListItems[position])
    }

    override fun getItemCount(): Int {
        return mListItems.size
    }

    fun showItems(items: List<FeedItem>) {
        this.mListItems.clear()
        this.mListItems.addAll(items)
        notifyDataSetChanged()
    }

    private inner class ViewHolder(private val dataBinding: ListItemTransactionBinding) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(item: FeedItem) {
            dataBinding.dateFormat = mDateFormatter
            dataBinding.item = item
        }
    }
}
