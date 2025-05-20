package com.example.untitled_capstone.data

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME


@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val newsAppDispatchers: AppDispatchers)

enum class AppDispatchers {
    IO,
}