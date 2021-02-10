package com.gchristov.sparechange.ui.fragments.savingsgoals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gchristov.sparechange.databinding.ListItemSavingsGoalBinding
import com.gchristov.sparechange.databinding.ListItemSavingsGoalChooseBinding
import com.gchristov.sparechange.repository.model.SavingsGoal
import java.util.*

class SavingsGoalAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mListItems = ArrayList<SavingsGoal>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(ListItemSavingsGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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

    fun showItems(items: List<SavingsGoal>) {
        this.mListItems.clear()
        this.mListItems.addAll(items)
        notifyDataSetChanged()
    }

    private inner class ViewHolder(private val dataBinding: ListItemSavingsGoalBinding) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(item: SavingsGoal) {
            dataBinding.item = item
        }
    }
}

class SavingsGoalChooseAdapter(private val listener: (item: SavingsGoal) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mListItems = ArrayList<SavingsGoal>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(ListItemSavingsGoalChooseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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

    fun showItems(items: List<SavingsGoal>) {
        this.mListItems.clear()
        this.mListItems.addAll(items)
        notifyDataSetChanged()
    }

    private inner class ViewHolder(private val dataBinding: ListItemSavingsGoalChooseBinding) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(item: SavingsGoal) {
            dataBinding.item = item
            // Clicks
            dataBinding.root.setOnClickListener {
                listener(item)
            }
        }
    }
}
