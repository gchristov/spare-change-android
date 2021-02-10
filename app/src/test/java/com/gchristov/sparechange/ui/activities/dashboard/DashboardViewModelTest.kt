package com.gchristov.sparechange.ui.activities.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gchristov.sparechange.common.fromMinorUnit
import com.gchristov.sparechange.repository.Repository
import com.gchristov.sparechange.repository.model.Account
import com.gchristov.sparechange.repository.model.Amount
import com.gchristov.sparechange.repository.model.FeedItem
import com.gchristov.sparechange.repository.model.SavingsGoal
import com.gchristov.sparechange.util.RxSchedulerRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class DashboardViewModelTest {

    @Rule
    @JvmField val rxSchedulerRule = RxSchedulerRule()
    @Rule
    @JvmField val rule = InstantTaskExecutorRule()

    private val repository = mock<Repository>()
    private val response = mock<ResponseBody>()

    private val viewModel get() = DashboardViewModel(repository)

    @Test
    fun test_init() {
        whenever(repository.getAccounts()).thenReturn(Single.just(arrayListOf(getAccount())))
        whenever(repository.getFeed(any(), any(), any())).thenReturn(Single.just(arrayListOf(getFeedItem(), getFeedItem())))
        whenever(repository.getSavingsGoals(any())).thenReturn(Single.just(arrayListOf(getSavingsGoal())))
        viewModel
        verify(repository).getAccounts()
        verify(repository).getFeed(any(), any(), any())
        verify(repository).getSavingsGoals(any())
        Assert.assertNotNull(viewModel.state.value)
        Assert.assertNotNull(viewModel.state.value?.account)
        Assert.assertTrue(viewModel.state.value?.feedItems?.isNotEmpty() == true)
        Assert.assertTrue(viewModel.state.value?.savingsGoals?.isNotEmpty() == true)
        Assert.assertNotNull(viewModel.state.value?.roundUpAmount)
        Assert.assertEquals("1.32", viewModel.state.value?.roundUpAmount?.minorUnits?.toPlainString())
    }

    @Test
    fun test_empty() {
        whenever(repository.getAccounts()).thenReturn(Single.just(arrayListOf(getAccount())))
        whenever(repository.getFeed(any(), any(), any())).thenReturn(Single.just(emptyList()))
        whenever(repository.getSavingsGoals(any())).thenReturn(Single.just(emptyList()))
        viewModel
        verify(repository).getAccounts()
        verify(repository).getFeed(any(), any(), any())
        verify(repository).getSavingsGoals(any())
        Assert.assertNotNull(viewModel.state.value)
        Assert.assertNotNull(viewModel.state.value?.account)
        Assert.assertTrue(viewModel.state.value?.feedItems?.isEmpty() == true)
        Assert.assertTrue(viewModel.state.value?.savingsGoals?.isEmpty() == true)
        Assert.assertNotNull(viewModel.state.value?.roundUpAmount)
        Assert.assertEquals("0", viewModel.state.value?.roundUpAmount?.minorUnits?.toPlainString())
    }

    @Test
    fun test_savingsGoal_create() {
        whenever(repository.getAccounts()).thenReturn(Single.just(arrayListOf(getAccount())))
        whenever(repository.getFeed(any(), any(), any())).thenReturn(Single.just(emptyList()))
        whenever(repository.getSavingsGoals(any())).thenReturn(Single.just(emptyList()))
        whenever(repository.createSavingsGoal(any(), any(), any())).thenReturn(Single.just(response))
        viewModel.onSavingsGoalCreate("name", Amount("GBP", BigDecimal("12.34")))
        verify(repository).createSavingsGoal(any(), eq("name"), eq(Amount("GBP", BigDecimal("12.34"))))
        // Expect two times because of refresh after successful operation
        verify(repository, times(2)).getAccounts()
        verify(repository, times(2)).getFeed(any(), any(), any())
        verify(repository, times(2)).getSavingsGoals(any())
    }

    @Test
    fun test_savingsGoal_fund() {
        whenever(repository.getAccounts()).thenReturn(Single.just(arrayListOf(getAccount())))
        whenever(repository.getFeed(any(), any(), any())).thenReturn(Single.just(emptyList()))
        whenever(repository.getSavingsGoals(any())).thenReturn(Single.just(emptyList()))
        whenever(repository.fundSavingsGoal(any(), any(), any(), any())).thenReturn(Single.just(response))
        viewModel.onSavingsGoalFund(getSavingsGoal())
        verify(repository).fundSavingsGoal(any(), eq(getSavingsGoal()), any(), any())
        // Expect two times because of refresh after successful operation
        verify(repository, times(2)).getAccounts()
        verify(repository, times(2)).getFeed(any(), any(), any())
        verify(repository, times(2)).getSavingsGoals(any())
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
                Amount("GBP", BigDecimal("1234").fromMinorUnit()),
                Amount("GBP", BigDecimal("1234").fromMinorUnit()),
                "OUT",
                Date(),
                "My Store"
        )
    }

    private fun getSavingsGoal(): SavingsGoal {
        return SavingsGoal(
                "id",
                "car",
                Amount("GBP", BigDecimal("1234").fromMinorUnit()),
                0
        )
    }
}