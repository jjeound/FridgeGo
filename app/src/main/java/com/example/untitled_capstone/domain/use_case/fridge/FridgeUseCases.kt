package com.example.untitled_capstone.domain.use_case.fridge

data class FridgeUseCases(
    val addFridgeItem: AddFridgeItem,
    val deleteFridgeItem: DeleteFridgeItem,
    val toggleNotification: ToggleNotification,
    val modifyFridgeItems: ModifyFridgeItem,
    val getFridgeItems: GetFridgeItems,
    val getFridgeItemById: GetFridgeItemById,
    val getFridgeItemsByDate: GetFridgeItemsByDate,
)
