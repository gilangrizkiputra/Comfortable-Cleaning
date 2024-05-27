package com.example.comfortablecleaning_copy.ui.Entitas

data class Pesanan(
    val idPesanan: String? = null,
    val namaPemesan: String? = null,
    val namaProduk: String? = null,
    val quantity: Int? = null,
    val noTelpPemesan: String? = null,
    val daerahPemesan: String? = null,
    val alamatPemesan: String? = null,
    val catatanPemesan: String? = null,
    val ongkir: Int? = null,
    val totalHarga: Int? = null
)