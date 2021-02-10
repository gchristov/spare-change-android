package com.gchristov.sparechange.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.gchristov.sparechange.ui.activities.dashboard.DashboardActivity
import com.gchristov.sparechange.ui.activities.dashboard.DashboardViewModel

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected val activityDashboard: DashboardActivity by lazy {
        activity as DashboardActivity
    }
    private var mDashboardState: DashboardViewModel.State? = null
    protected lateinit var mDataBinding: T

    @LayoutRes abstract fun provideLayoutResId(): Int
    @ColorRes abstract fun provideStatusBarColorResId(): Int
    @StringRes abstract fun provideTitleResId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupDashboardState()
        setupDataBinding(inflater, container)
        return mDataBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        mDashboardState?.let { renderState(it) }
    }

    open fun renderState(state: DashboardViewModel.State) {
        mDashboardState = state
    }

    // Setup

    internal open fun setupDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        mDataBinding = DataBindingUtil.inflate(inflater, provideLayoutResId(), container, false)
    }

    private fun setupDashboardState() {
        mDashboardState = activityDashboard.dashboardState()
    }
}