package com.example.CH2_PS020.fitsync.ui.historyWorkout

import android.os.Parcelable
import com.example.CH2_PS020.fitsync.api.response.Duration
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryModel(
    val duration: Duration? = null,
    val gif: String? = null,
    val jpg: String? = null,
    val level:String? = null,
    val title: String? = null,
    val type: String? = null,
    val bodyPart: String? = null,
    val desc: String? = null,
    val dateDone: String? = null,
    val id: String? = null
):Parcelable
