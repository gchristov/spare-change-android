package com.gchristov.sparechange.repository

import com.gchristov.sparechange.api.StarlingService
import com.gchristov.sparechange.api.model.*
import com.gchristov.sparechange.repository.model.Account
import com.gchristov.sparechange.repository.model.Amount
import com.gchristov.sparechange.repository.model.FeedItem
import com.gchristov.sparechange.repository.model.SavingsGoal
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import okhttp3.ResponseBody
import org.apache.commons.lang3.time.FastDateFormat
import org.junit.Test
import org.mockito.ArgumentMatchers
import java.math.BigDecimal
import java.util.*

class RepositoryTest {

    private val apiService = mock<StarlingService>()
    private val dateFormat = mock<FastDateFormat>()
    private val responseBody = mock<ResponseBody>()

    private val repository get() = Repository(apiService, dateFormat)

    @Test
    fun test_getAccounts() {
        whenever(apiService.getAccounts()).thenReturn(Single.just(getAccountsResponse()))
        repository.getAccounts()
        verify(apiService).getAccounts()
    }

    @Test
    fun test_getFeed() {
        whenever(dateFormat.format(ArgumentMatchers.any<Date>())).thenReturn("123456789")
        whenever(apiService.getFeed(any(), any(), any())).thenReturn(Single.just(getFeedResponse()))
        repository.getFeed("aUid", "cUid", Date())
        verify(apiService).getFeed(eq("aUid"), eq("cUid"), eq("123456789"))
    }

    @Test
    fun test_getSavingsGoals() {
        whenever(apiService.getSavingsGoals(any())).thenReturn(Single.just(getSavingsGoalsResponse()))
        repository.getSavingsGoals("aUid")
        verify(apiService).getSavingsGoals(eq("aUid"))
    }

    @Test
    fun test_createSavingsGoal() {
        whenever(apiService.createSavingsGoal(any(), any())).thenReturn(Single.just(responseBody))
        repository.createSavingsGoal("aUid", "name", Amount("GBP", BigDecimal("12.34")))
        verify(apiService).createSavingsGoal(eq("aUid"), eq(ApiSavingsGoalCreateRequest("name", "GBP", Amount("GBP", BigDecimal("12.34")))))
    }

    @Test
    fun test_fundSavingsGoal() {
        whenever(apiService.fundSavingsGoal(any(), any(), any(), any())).thenReturn(Single.just(responseBody))
        val transactionUid = UUID.randomUUID().toString()
        repository.fundSavingsGoal("aUid", getSavingsGoal(), transactionUid, Amount("GBP", BigDecimal("12.34")))
        verify(apiService).fundSavingsGoal(eq("aUid"), eq("id"), eq(transactionUid), eq(ApiSavingsGoalFundRequest(Amount("GBP", BigDecimal("12.34")))))
    }

    private fun getAccountsResponse(): ApiAccountsResponse {
        return ApiAccountsResponse(arrayListOf(getAccount()))
    }

    private fun getFeedResponse(): ApiFeedResponse {
        return ApiFeedResponse(arrayListOf(getFeedItem()))
    }

    private fun getSavingsGoalsResponse(): ApiSavingsGoalsResponse {
        return ApiSavingsGoalsResponse(arrayListOf(getSavingsGoal()))
    }

    private fun getAccount(): Account {
        return Account(
                "id",
                "category",
                "GBP",
                "name",
        )
    }

    private fun getFeedItem(): FeedItem {
        return FeedItem(
                "id",
                "category",
                Amount("GBP", BigDecimal("1234")),
                Amount("GBP", BigDecimal("1234")),
                "OUT",
                Date(),
                "My Store"
        )
    }

    private fun getSavingsGoal(): SavingsGoal {
        return SavingsGoal(
                "id",
                "car",
                Amount("GBP", BigDecimal("1234")),
                0
        )
    }
}