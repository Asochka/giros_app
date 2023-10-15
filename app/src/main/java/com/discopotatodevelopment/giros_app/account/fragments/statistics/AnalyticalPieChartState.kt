package com.discopotatodevelopment.giros_app.account.fragments.statistics

import android.os.Parcelable
import android.view.View

/**
 * Собственный state для сохранения и восстановления данных
 */

class AnalyticalPieChartState(
    private val superSavedState: Parcelable?,
    val dataList: List<Pair<Int, String>>
    ) : View.BaseSavedState(superSavedState), Parcelable {
}