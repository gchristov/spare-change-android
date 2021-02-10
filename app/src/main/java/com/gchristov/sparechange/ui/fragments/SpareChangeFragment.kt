package com.gchristov.sparechange.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gchristov.sparechange.R
import com.gchristov.sparechange.databinding.FragmentSpareChangeBinding
import com.gchristov.sparechange.ui.activities.dashboard.DashboardViewModel
import com.gchristov.sparechange.ui.base.BaseFragment

class SpareChangeFragment : BaseFragment<FragmentSpareChangeBinding>() {

    override fun provideLayoutResId(): Int {
        return R.layout.fragment_spare_change
    }

    override fun provideStatusBarColorResId(): Int {
        return R.color.accent
    }

    override fun provideTitleResId(): Int {
        return R.string.spare_change_title
    }

    // Render

    override fun renderState(state: DashboardViewModel.State) {
        super.renderState(state)
        if (!isAdded) {
            // Do not render until fragment is attached
            return
        }
        mDataBinding.state = state
    }

    // Setup

    override fun setupDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        super.setupDataBinding(inflater, container)
        mDataBinding.activity = activityDashboard
    }
}