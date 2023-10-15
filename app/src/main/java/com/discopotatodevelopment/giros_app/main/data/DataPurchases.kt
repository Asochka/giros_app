package com.discopotatodevelopment.giros_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.util.*

@Parcelize
data class DataPurchases(
    val id_check: Int,
    val date_purch_check: Timestamp,
    val total_amount_check: Float,
    val calories_check: Float,
    val dishes_in_check: String): Parcelable