package com.gchristov.sparechange.api.model

import com.gchristov.sparechange.repository.model.Account
import com.gchristov.sparechange.repository.model.FeedItem
import com.gchristov.sparechange.repository.model.SavingsGoal

data class ApiAccountsResponse(val accounts: List<Account>)

data class ApiFeedResponse(val feedItems: List<FeedItem>)

data class ApiSavingsGoalsResponse(val savingsGoalList: List<SavingsGoal>)