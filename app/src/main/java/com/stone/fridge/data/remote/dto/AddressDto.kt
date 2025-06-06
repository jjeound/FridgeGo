package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.Address

data class AddressDto(
    val address_name: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val mountain_yn: String,
    val main_address_no: String,
    val sub_address_no: String,
){
    fun toAddress(): Address{
        return Address(
            regionGu = region_2depth_name,
            regionDong = region_3depth_name
        )
    }
}
