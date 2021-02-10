package com.gchristov.sparechange.ui.fragments.savingsgoals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gchristov.sparechange.R
import com.gchristov.sparechange.databinding.FragmentSavingsGoalsBinding
import com.gchristov.sparechange.ui.activities.dashboard.DashboardViewModel
import com.gchristov.sparechange.ui.base.BaseFragment

class SavingsGoalsFragment : BaseFragment<FragmentSavingsGoalsBinding>() {

    private lateinit var mAdapter: SavingsGoalAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // UI needs setup before state is rendered
        setupLists()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun provideLayoutResId(): Int {
        return R.layout.fragment_savings_goals
    }

    override fun provideStatusBarColorResId(): Int {
        return R.color.button_light
    }

    override fun provideTitleResId(): Int {
        return R.string.savings_goals_title
    }

    // Render

    override fun renderState(state: DashboardViewModel.State) {
        super.renderState(state)
        if (!isAdded) {
            // Do not render until fragment is attached
            return
        }
        mDataBinding.state = state
        mAdapter.showItems(state.savingsGoals)
    }

    // Setup

    override fun setupDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        super.setupDataBinding(inflater, container)
        mDataBinding.activity = activityDashboard
    }

    private fun setupLists() {
        mAdapter = SavingsGoalAdapter()
        mDataBinding.recyclerView.adapter = mAdapter
    }
}