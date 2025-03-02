package com.example.untitled_capstone.navigation

import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.domain.model.Recipe
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface Screen {
    @Serializable
    data class ChattingRoomNav(val chattingRoom: ChattingRoom) : Screen{
        companion object {
            val typeMap = mapOf(typeOf<ChattingRoom>() to CustomNavType.ChatType)
        }
    }

    @Serializable
    data class RecipeNav(val recipe: Recipe) : Screen{
        companion object {
            val typeMap = mapOf(typeOf<Recipe>() to CustomNavType.RecipeType)
        }
    }

    @Serializable
    data class PostNav(val post: Post) : Screen {
        companion object {
            val typeMap = mapOf(typeOf<Post>() to CustomNavType.PostType)
        }
    }

    @Serializable
    data object WritingNav: Screen

    @Serializable
    data object NotificationNav: Screen

    @Serializable
    data object OnBoarding: Screen

    @Serializable
    data object AddFridgeItemNav: Screen

    @Serializable
    data object  LoginNav: Screen

    @Serializable
    data object Home: Screen

    @Serializable
    data object Shopping: Screen

    @Serializable
    data object Fridge: Screen

    @Serializable
    data object Chat: Screen

    @Serializable
    data object My: Screen
}

sealed interface Graph {
    @Serializable
    data object HomeGraph: Graph

    @Serializable
    data object ShoppingGraph: Graph

    @Serializable
    data object FridgeGraph: Graph

    @Serializable
    data object ChatGraph: Graph

    @Serializable
    data object MyGraph: Graph
}
