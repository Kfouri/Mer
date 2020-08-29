package com.kfouri.mercadotest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kfouri.mercadotest.viewmodel.SearchActivityViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchProductTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = SearchActivityViewModel()

    @Test
    fun searchProductsObteiningResultTest() {

        viewModel.searchProduct("Moto G6")

        Assert.assertNotEquals(
            viewModel.onProductList().getOrAwaitValue().size,
            0
        )
    }

    @Test
    fun searchProductsNotObteiningResultTest() {

        viewModel.searchProduct("asdqwe")

        Assert.assertEquals(
            viewModel.onProductList().getOrAwaitValue().size,
            0
        )
    }
}