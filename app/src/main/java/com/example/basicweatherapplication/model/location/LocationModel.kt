package com.example.basicweatherapplication.model.location

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(
    val name: String,
) : Parcelable