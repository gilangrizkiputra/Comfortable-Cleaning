package com.example.comfortablecleaning_copy.ui.Entitas

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Produk(
    var idProduk: String? = null,
    var jenis: String? = null,
    var namaProduk: String? = null,
    var harga: Int? = null,
    var estimasi: String? = null,
    var deskripsi: String? = null,
    var imageUrl: String? = null
) : Parcelable