package com.ariastormtechnologies.guardiantrack.utils

import android.os.Handler
import android.os.Looper
import android.view.View

class SOSFlashOverlay {

    private var sosHandler: Handler? = null
    private var step = 0
    private var isFlashing = false

    private val pattern = listOf(
        300L, 300L, 300L,   // S (3 cortos)
        900L, 900L, 900L,   // O (3 largos)
        300L, 300L, 300L    // S (3 cortos)
    )

    fun start(view: View) {
        if (isFlashing) return // Evita llamadas múltiples

        isFlashing = true
        sosHandler = Handler(Looper.getMainLooper())
        step = 0
        flashStep(view)
    }

    private fun flashStep(view: View) {
        if (!isFlashing) return

        if (step >= pattern.size) {
            // Espera 1 segundo y reinicia
            sosHandler?.postDelayed({
                step = 0
                flashStep(view)
            }, 1000)
            return
        }

        val duration = pattern[step]
        view.visibility = if (step % 2 == 0) View.VISIBLE else View.INVISIBLE

        sosHandler?.postDelayed({
            step++
            flashStep(view)
        }, duration)
    }

    fun stop(view: View) {
        isFlashing = false
        sosHandler?.removeCallbacksAndMessages(null)
        sosHandler = null
        view.clearAnimation()
        view.visibility = View.GONE
    }
}
