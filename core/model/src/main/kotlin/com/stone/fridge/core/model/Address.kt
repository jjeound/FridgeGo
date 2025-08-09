package com.stone.fridge.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Address(
    @SerialName("address_name")val addressName: String,
    @SerialName("region_1depth_name")val region1depthName: String,
    @SerialName("region_2depth_name")val region2depthName: String,
    @SerialName("region_3depth_name")val region3depthName: String,
    @SerialName("mountain_yn") val mountainYn: String,
    @SerialName("main_address_no") val mainAddressNo: String,
    @SerialName("sub_address_no") val subAddressNo: String,
)

@Serializable
data class AddressInfo(
    val regionGu : String,
    val regionDong : String,
)

fun Address.toAddressInfo(): AddressInfo {
    return AddressInfo(
        regionGu = region2depthName,
        regionDong = region3depthName
    )
}