package com.stone.fridge.core.test.data

import com.stone.fridge.core.model.Recipe

val recipeData: Recipe = Recipe(
    id = 1L,
    title = "감자조림",
    imageUrl = null,
    liked = true,
    instructions = "1. 감자를 썰어 간장과 설탕에 조린다.\n2. 중불에서 20분간 끓인다.",
    ingredients = listOf("감자", "간장", "설탕"),
)