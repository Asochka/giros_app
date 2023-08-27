package com.discopotatodevelopment.giros_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class DataPurchases(
    val id_check: Int,
    val date_purch: Date,
    val total_amount: Float): Parcelable
