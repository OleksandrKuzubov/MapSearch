package com.kutuzov.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kutuzov.domain.model.FavoriteAddress
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails

@Entity(tableName = "favorites")
data class AddressDto(
    @PrimaryKey
    val id: String,
    val streetNumber: String?,
    val street: String?,
    val streetName: String?,
    val municipality: String?,
    val postalCode: String?,
    val countryCode: String?,
    val country: String?,
    val freeFormAddress: String?
) {
    companion object {
        fun from(item: FuzzySearchDetails) =
            AddressDto(
                item.id,
                item.address?.streetNumber,
                item.address?.street,
                item.address?.streetName,
                item.address?.municipality,
                item.address?.postalCode,
                item.address?.countryCode,
                item.address?.country,
                item.address?.freeFormAddress
            )
    }

    fun toFavoriteAddress() =
        FavoriteAddress(
            id,
            streetNumber,
            street,
            streetName,
            municipality,
            postalCode,
            countryCode,
            country,
            freeFormAddress
        )
}
