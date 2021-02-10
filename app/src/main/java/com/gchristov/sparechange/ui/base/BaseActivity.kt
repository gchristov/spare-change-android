package com.gchristov.sparechange.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var mDataBinding: T

    @LayoutRes abstract fun provideLayoutResId(): Int

    abstract fun setupViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setupViewModel()
    }

    // Setup

    internal open fun setupDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, provideLayoutResId())
    }
}