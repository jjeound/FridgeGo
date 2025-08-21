package com.stone.fridge.core.test.data

import com.stone.fridge.core.model.Fridge

val fridgesData: List<Fridge> = listOf(
    Fridge(
        id = 0L,
        foodName = "감자",
        imageUrl = null,
        count = 3,
        alarmStatus = false,
        useByDate = 20102123,
        storageType = true
    ),
)