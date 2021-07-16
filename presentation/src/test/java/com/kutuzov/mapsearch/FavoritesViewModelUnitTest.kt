package com.kutuzov.mapsearch

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kutuzov.domain.model.FavoriteAddress
import com.kutuzov.domain.repo.SearchRepository
import com.kutuzov.mapsearch.di.module.dbModule
import com.kutuzov.mapsearch.di.module.networkModule
import com.kutuzov.mapsearch.di.module.repositoryModule
import com.kutuzov.mapsearch.di.module.viewModelModule
import com.kutuzov.mapsearch.ui.favorites.FavoritesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.koin.test.AutoCloseKoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class FavoritesViewModelUnitTest : AutoCloseKoinTest() {

    @ExperimentalCoroutinesApi
    private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true

                override fun postToMainThread(runnable: Runnable) = runnable.run()
            })
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        ArchTaskExecutor.getInstance()
            .setDelegate(null)
    }

    private val favoritesViewModel: FavoritesViewModel by inject()
    private val searchRepository: SearchRepository by inject()

    @get:Rule
    val koinRule = KoinTestRule.create {
        modules(
            listOf(
                dbModule,
                networkModule,
                repositoryModule,
                viewModelModule
            )
        )
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Test
    fun test_load_favorites() = runBlocking {

        val response = listOf(
            FavoriteAddress(id = "trololo"),
            FavoriteAddress(id = "trololi")
        )

        declareMock<SearchRepository> {
            Mockito.`when`(searchRepository.getFavorites()).thenReturn(response)
        }

        favoritesViewModel.getFavorites()

        assertEquals(favoritesViewModel.favorites.getOrAwaitValue(), response)
    }

    @Test
    fun test_remove_address() = runBlocking {

        val response = listOf(
            FavoriteAddress(id = "trololo"),
            FavoriteAddress(id = "trololi")
        )

        declareMock<SearchRepository> {
            Mockito.`when`(searchRepository.getFavorites()).thenReturn(response)
            Mockito.`when`(searchRepository.removeAddressById(Mockito.anyString())).thenReturn(Unit)
        }

        favoritesViewModel.removeAddress(FavoriteAddress(id = "trololo"))

        assertEquals(favoritesViewModel.favorites.getOrAwaitValue(), response)
    }
}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}