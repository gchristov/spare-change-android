package com.gchristov.sparechange.repository.model

import java.io.Serializable

data class SavingsGoal(
        val savingsGoalUid: String,
        val name: String,
        val totalSaved: Amount,
        val savedPercentage: Int
) : Serializable // TODO: Replace with Parcelable for better performance - okay for demo
