package com.example.comfortablecleaning_copy.ui.Entitas

import com.google.android.gms.common.api.Status

data class Pesanan(
    var idPesanan: String? = null,
    var namaPemesan: String? = null,
    var namaProduk: String? = null,
    var quantity: Int? = null,
    var noTelpPemesan: String? = null,
    var daerahPemesan: String? = null,
    var alamatPemesan: String? = null,
    var catatanPemesan: String? = null,
    var ongkir: Int? = null,
    var totalHarga: Int? = null,
    var status: String = "Menunggu"
)