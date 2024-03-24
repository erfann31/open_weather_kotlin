package com.example.open_weater_kotlin_uiutil.coroutine

import kotlinx.coroutines.Dispatchers

object CoroutineDispatchersImpl : CoroutineDispatchers {
    override val io = Dispatchers.IO
    override val main = Dispatchers.Main
}
