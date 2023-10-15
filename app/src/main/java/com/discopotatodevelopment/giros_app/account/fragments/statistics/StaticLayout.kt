package com.discopotatodevelopment.giros_app.account.fragments.statistics

import android.graphics.Canvas
import android.text.StaticLayout
import androidx.core.graphics.withTranslation

/**
 * StaticLayout Extension для удобства отрисовки текста.
 */
fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
    canvas.withTranslation(x, y) {
        draw(this)
    }
}