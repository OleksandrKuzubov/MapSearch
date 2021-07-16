package com.kutuzov.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteAddress(
    val id: String,
    val streetNumber: String? = null,
    val street: String? = null,
    val streetName: String? = null,
    val municipality: String? = null,
    val postalCode: String? = null,
    val countryCode: String? = null,
    val country: String? = null,
    val freeFormAddress: String? = null,
    var isExpanded: Boolean = false
) : Parcelable