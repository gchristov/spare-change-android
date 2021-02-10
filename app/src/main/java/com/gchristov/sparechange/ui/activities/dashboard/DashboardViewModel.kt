package com.gchristov.sparechange.ui.activities.dashboard

import com.gchristov.sparechange.common.SingleLiveEvent
import com.gchristov.sparechange.common.weeksFromNow
import com.gchristov.sparechange.repository.Repository
import com.gchristov.sparechange.repository.model.Account
import com.gchristov.sparechange.repository.model.Amount
import com.gchristov.sparechange.repository.model.FeedItem
import com.gchristov.sparechange.repository.model.SavingsGoal
import com.gchristov.sparechange.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

class DashboardViewModel(private val repository: Repository) : BaseViewModel<DashboardViewModel.State>(State()) {

    // Events

    val eventError = SingleLiveEvent<Throwable>()
    val eventGoalFunded = SingleLiveEvent<Void>()

    init {
        onRefresh()
    }

    fun hasSavingsGoals() = state.value?.savingsGoals?.isNotEmpty() == true

    fun onRefresh() {
        if (state.value?.refreshing == true) {
            // Already loading
            return
        }
        setState { copy(refreshing = true) }
        var tempAccount: Account? = null // Keep track of loaded account to update state with
        var tempFeed: List<FeedItem>? = null // Keep track of loaded transactions feed to update state with
        var tempSavingsGoals: List<SavingsGoal>? = null // Keep track of loaded savings goals to update state with
        disposables.add(
                // Step 1: Load user accounts
                repository.getAccounts()
                        .flatMap {
                            // Step 2: Load transactions feed
                            tempAccount = it.first() // TODO: There might be more accounts so use first one for this exercise
                            val oneWeekAgo = Date().weeksFromNow(-1) // TODO: Show only last week for this exercise
                            repository.getFeed(tempAccount!!.accountUid, tempAccount!!.defaultCategory, oneWeekAgo)
                        }
                        .flatMap {
                            // Step 3: Load savings goals
                            tempFeed = it
                            repository.getSavingsGoals(tempAccount!!.accountUid)
                        }
                        .flatMap {
                            // Step 4: Round-up transactions
                            tempSavingsGoals = it
                            calculateWeeklyRoundUp(tempAccount!!.currency, tempFeed!!)
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ roundUpAmount ->
                            setState { copy(refreshing = false,
                                    account = tempAccount,
                                    feedItems = tempFeed ?: emptyList(),
                                    savingsGoals = tempSavingsGoals ?: emptyList(),
                                    roundUpAmount = roundUpAmount) }
                        }, { throwable ->
                            throwable.printStackTrace()
                            setState { copy(refreshing = false) }
                            eventError.value = throwable
                        }))
    }

    fun onSavingsGoalCreate(
        name: String,
        target: Amount
    ) {
        if (state.value?.refreshing == true) {
            // Already loading
            return
        } else if (state.value?.account == null) {
            eventError.value = Throwable("Cannot create savings goal due to missing account")
            return
        }
        setState { copy(refreshing = true) }
        disposables.add(
                repository.createSavingsGoal(state.value!!.account!!.accountUid, name, target)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            // Refresh info after savings goal has been created
                            setState { copy(refreshing = false) }
                            onRefresh()
                        }, { throwable ->
                            throwable.printStackTrace()
                            setState { copy(refreshing = false) }
                            eventError.value = throwable
                        }))
    }

    fun onSavingsGoalFund(savingsGoal: SavingsGoal) {
        if (state.value?.refreshing == true) {
            // Already loading
            return
        }
        setState { copy(refreshing = true) }
        val transactionUid = UUID.randomUUID().toString()
        disposables.add(
                repository.fundSavingsGoal(state.value!!.account!!.accountUid, savingsGoal, transactionUid, state.value!!.roundUpAmount!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            // Refresh info after savings goal has been created
                            setState { copy(refreshing = false) }
                            onRefresh()
                            eventGoalFunded.call()
                        }, { throwable ->
                            throwable.printStackTrace()
                            setState { copy(refreshing = false) }
                            eventError.value = throwable
                        }))
    }

    private fun calculateWeeklyRoundUp(
        currency: String,
        transactions: List<FeedItem>
    ): Single<Amount> {
        return Single.fromCallable {
            var units = BigDecimal(0)
            transactions
                    .filter { it.isOut() } // Only round-up outgoing transactions
                    .forEach {
                units = units.add(it.spareChangeRoundUp().minorUnits)
            }
            Amount(currency, units)
        }
    }

    data class State(
        val refreshing: Boolean = false,
        val account: Account? = null,
        val feedItems: List<FeedItem> = emptyList(),
        val savingsGoals: List<SavingsGoal> = emptyList(),
        val roundUpAmount: Amount? = null,
    ) : Serializable // TODO: Should ideally be Parcelable but this is simpler to illustrate the concept
}
