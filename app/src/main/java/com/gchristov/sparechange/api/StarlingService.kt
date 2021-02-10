package com.gchristov.sparechange.api

import com.gchristov.sparechange.api.model.*
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface StarlingService {

    // Accounts

    @GET("api/v2/accounts")
    fun getAccounts(): Single<ApiAccountsResponse>

    // Transactions

    @GET("api/v2/feed/account/{accountUid}/category/{categoryUid}")
    fun getFeed(
        @Path("accountUid") accountUid: String,
        @Path("categoryUid") categoryUid: String,
        @Query("changesSince") changesSince: String
    ): Single<ApiFeedResponse>

    // Savings goals

    @GET("api/v2/account/{accountUid}/savings-goals")
    fun getSavingsGoals(@Path("accountUid") accountUid: String): Single<ApiSavingsGoalsResponse>

    @PUT("api/v2/account/{accountUid}/savings-goals")
    fun createSavingsGoal(
        @Path("accountUid") accountUid: String,
        @Body request: ApiSavingsGoalCreateRequest
    ): Single<ResponseBody> // For now, no need to parse body here, 200 response code is enough

    @PUT("api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    fun fundSavingsGoal(
        @Path("accountUid") accountUid: String,
        @Path("savingsGoalUid") savingsGoalUid: String,
        @Path("transferUid") transferUid: String,
        @Body request: ApiSavingsGoalFundRequest
    ): Single<ResponseBody> // For now, no need to parse body here, 200 response code is enough
}
