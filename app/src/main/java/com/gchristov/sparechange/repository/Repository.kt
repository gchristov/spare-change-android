package com.gchristov.sparechange.repository

import com.gchristov.sparechange.api.StarlingService
import com.gchristov.sparechange.api.model.ApiSavingsGoalCreateRequest
import com.gchristov.sparechange.api.model.ApiSavingsGoalFundRequest
import com.gchristov.sparechange.repository.model.Account
import com.gchristov.sparechange.repository.model.Amount
import com.gchristov.sparechange.repository.model.FeedItem
import com.gchristov.sparechange.repository.model.SavingsGoal
import io.reactivex.Single
import okhttp3.ResponseBody
import org.apache.commons.lang3.time.FastDateFormat
import java.util.*

class Repository(private val apiService: StarlingService,
                 private val dateFormat: FastDateFormat) {

    fun getAccounts(): Single<List<Account>> {
        return apiService.getAccounts()
                .map { it.accounts }
    }

    fun getFeed(
        accountUid: String,
        categoryUid: String,
        changesSince: Date
    ): Single<List<FeedItem>> {
        return apiService.getFeed(accountUid, categoryUid, dateFormat.format(changesSince))
                .map { it.feedItems }
    }

    fun getSavingsGoals(accountUid: String): Single<List<SavingsGoal>> {
        return apiService.getSavingsGoals(accountUid)
                .map { it.savingsGoalList }
    }

    fun createSavingsGoal(
        accountUid: String,
        name: String,
        target: Amount
    ): Single<ResponseBody> {
        return apiService.createSavingsGoal(accountUid, ApiSavingsGoalCreateRequest(name, target.currency, target))
    }

    fun fundSavingsGoal(
        accountUid: String,
        savingsGoal: SavingsGoal,
        transactionUid: String,
        amount: Amount
    ): Single<ResponseBody> {
        return apiService.fundSavingsGoal(accountUid, savingsGoal.savingsGoalUid, transactionUid, ApiSavingsGoalFundRequest(amount))
    }
}
