package com.gchristov.sparechange.ui.activities.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.gchristov.sparechange.R
import com.gchristov.sparechange.databinding.ActivityDashboardBinding
import com.gchristov.sparechange.ui.base.BaseActivity
import com.gchristov.sparechange.ui.dialogs.InputDialogs
import com.gchristov.sparechange.ui.fragments.SpareChangeFragment
import com.gchristov.sparechange.ui.fragments.savingsgoals.SavingsGoalsFragment
import com.gchristov.sparechange.ui.fragments.transactions.TransactionsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    private val mViewModel by viewModel<DashboardViewModel>()

    private lateinit var mAdapter: DashboardPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    override fun provideLayoutResId(): Int {
        return R.layout.activity_dashboard
    }

    fun dashboardState(): DashboardViewModel.State? {
        return mViewModel.state.value
    }

    fun refresh() {
        mViewModel.onRefresh()
    }

    fun startCreateSavingsGoalFlow() {
        InputDialogs.showSavingsGoalCreateDialog(this) { name, target ->
            mViewModel.onSavingsGoalCreate(name, target)
        }
    }

    fun startFundSavingsGoalFlow() {
        if (mViewModel.hasSavingsGoals()) {
            mViewModel.state.value?.savingsGoals?.let {
                InputDialogs.showSavingsGoalChooseDialog(this, it) { savingsGoal ->
                    mViewModel.onSavingsGoalFund(savingsGoal)
                }
            }
        } else {
            // No savings goals yet
            startCreateSavingsGoalFlow()
        }
    }

    // Render

    private fun renderState(state: DashboardViewModel.State) {
        renderNavigation(state)
    }

    private fun renderNavigation(state: DashboardViewModel.State) {
        for (i in 0 until mAdapter.count) {
            mAdapter.getItem(i).renderState(state)
        }
        renderStatusBar(mDataBinding.pager.currentItem)
    }

    private fun renderStatusBar(position: Int) {
        window.statusBarColor = ContextCompat.getColor(this, mAdapter.getPageStatusBarColor(position))
    }

    // Setup

    override fun setupViewModel() {
        mViewModel.state.observe(this) { state ->
            renderState(state)
        }
        mViewModel.eventError.observe(this) { error ->
            error.message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
        mViewModel.eventGoalFunded.observe(this) {
            Toast.makeText(this, R.string.spare_change_funded, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNavigation() {
        // Navigation index mappings
        val mPagerToBottomNavIndex = hashMapOf(0 to R.id.spareChangeTab, 1 to R.id.transactionsTab, 2 to R.id.savingsGoalsTab)
        val mBottomNavToPagerIndex = mPagerToBottomNavIndex.entries.associateBy({ it.value }) { it.key }
        // ViewPager
        mAdapter = DashboardPagerAdapter(this, supportFragmentManager, arrayListOf(
                SpareChangeFragment(),
                TransactionsFragment(),
                SavingsGoalsFragment(),
        ))
        mDataBinding.pager.offscreenPageLimit = mAdapter.count - 1 // Fragments are static so it's okay to keep them in memory
        mDataBinding.pager.adapter = mAdapter
        mDataBinding.pager.clearOnPageChangeListeners()
        mDataBinding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                renderStatusBar(position)
                // Adjust BottomNavigation to match ViewPager index
                mPagerToBottomNavIndex[position]?.let {
                    if (mDataBinding.bottomNav.selectedItemId != it) {
                        mDataBinding.bottomNav.selectedItemId = it
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        // BottomNavigationView
        mDataBinding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            // Adjust ViewPager to match BottomNavigation index
            mBottomNavToPagerIndex[menuItem.itemId]?.let {
                if (mDataBinding.pager.currentItem != it) {
                    mDataBinding.pager.setCurrentItem(it, true)
                }
            }
            true
        }
    }
}
