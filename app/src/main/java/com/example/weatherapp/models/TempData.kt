package com.example.weatherapp.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TempData(val temp : String?, @SerializedName("feels_like")
val feelsLike : String?,
                    val humidity : String?) : Parcelable
