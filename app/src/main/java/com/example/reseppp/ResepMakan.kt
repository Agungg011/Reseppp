package com.example.reseppp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Serializable
data class ResepMakan(
    val id: Int,
    val gambar: String,
    @SerialName("nama_resep") val namaResep: String,
    val alat_dan_bahan: String,
    val langkah: String
)
