package com.gchristov.sparechange.api.model

import com.gchristov.sparechange.repository.model.Amount

data class ApiSavingsGoalCreateRequest(
        val name: String,
        val currency: String,
        val target: Amount,
)

data class ApiSavingsGoalFundRequest(
        val amount: Amount
)
