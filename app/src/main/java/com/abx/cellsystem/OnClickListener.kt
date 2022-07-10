package com.abx.cellsystem

import android.content.Intent

interface OnClickListener {
    fun onLongClick(celular: Celular, currentAdapter: CelularAdapter)
    fun onClickCelNumber(intent: Intent)
}