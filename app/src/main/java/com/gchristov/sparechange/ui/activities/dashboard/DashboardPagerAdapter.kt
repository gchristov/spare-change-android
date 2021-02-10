package com.gchristov.sparechange.ui.activities.dashboard

import android.content.Context
import androidx.annotation.ColorRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gchristov.sparechange.ui.base.BaseFragment
import java.lang.ref.WeakReference

class DashboardPagerAdapter(context: Context,
                            manager: FragmentManager,
                            private val items: List<BaseFragment<*>>) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mContext = WeakReference(context)

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): BaseFragment<*> {
        return items[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.get()?.getString(items[position].provideTitleResId())
    }

    @ColorRes fun getPageStatusBarColor(position: Int): Int {
        return items[position].provideStatusBarColorResId()
    }
}