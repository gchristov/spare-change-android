package com.gchristov.sparechange.repository.model

import java.io.Serializable

data class Account(
        val accountUid: String,
        val defaultCategory: String,
        val currency: String,
        val name: String,
) : Serializable // TODO: Replace with Parcelable for better performance - okay for demo
