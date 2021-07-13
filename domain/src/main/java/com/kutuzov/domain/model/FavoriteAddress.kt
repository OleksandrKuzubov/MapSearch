package com.kutuzov.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteAddress(
    val id: String,
    val streetNumber: String?,
    val street: String?,
    val streetName: String?,
    val municipality: String?,
    val postalCode: String?,
    val countryCode: String?,
    val country: String?,
    val freeFormAddress: String?
) : Parcelable