package com.example.whacacrocapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(

    var winner: Boolean,
    var countOfHits: Int
) : Parcelable
