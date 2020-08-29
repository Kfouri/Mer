package com.kfouri.mercadotest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kfouri.mercadotest.viewmodel.ProductDetailActivityViewModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ProductDetailTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = ProductDetailActivityViewModel()

    @Test
    fun findProductByIdTest() {

        viewModel.getProduct("MLA874539291")

        assertEquals(viewModel.onGetProduct().getOrAwaitValue().title, "Celular Motorola G6 Liberado")
    }

    @Test
    fun findProductByIdWithDescriptionTest() {

        // ID = MLA874539291 si tiene descripción

        viewModel.getProduct("MLA874539291")

        assertNotEquals(viewModel.onGetProduct().getOrAwaitValue().description, null)
    }

    @Test
    fun findProductByIdWithoutDescriptionTest() {

        // ID = MLA869639994 no tiene descripción

        viewModel.getProduct("MLA869639994")

        assertEquals(viewModel.onGetProduct().getOrAwaitValue().description, null)
    }
}
