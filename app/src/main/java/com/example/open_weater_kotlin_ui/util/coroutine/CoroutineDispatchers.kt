package com.example.open_weater_kotlin_uiutil.coroutine

import kotlin.coroutines.CoroutineContext

interface CoroutineDispatchers {
    val io: CoroutineContext
    val main: CoroutineContext
}
