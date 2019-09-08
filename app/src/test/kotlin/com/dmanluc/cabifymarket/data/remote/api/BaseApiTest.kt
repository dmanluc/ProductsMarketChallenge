package com.dmanluc.cabifymarket.data.remote.api

import com.dmanluc.cabifymarket.di.createRemoteModule
import com.dmanluc.cabifymarket.utils.MockDataProvider
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.get
import org.koin.test.AutoCloseKoinTest

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-31.
 */
abstract class BaseApiTest : AutoCloseKoinTest() {

    protected lateinit var apiService: MarketApi
    protected lateinit var mockServer: MockWebServer

    @Before
    open fun setUp() {
        configureMockServer()
        configureDi()
    }

    @After
    open fun tearDown() {
        stopMockServer()
    }

    private fun configureDi() {
        startKoin { modules(listOf(createRemoteModule(mockServer.url("/").toString()))) }
        apiService = get()
    }

    private fun configureMockServer() {
        mockServer = MockWebServer()
        mockServer.start()
    }

    private fun stopMockServer() {
        mockServer.shutdown()
    }

    fun mockHttpResponse(mockServer: MockWebServer, fileName: String, responseCode: Int): Unit =
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(MockDataProvider.readJsonAsString(fileName))
        )

}