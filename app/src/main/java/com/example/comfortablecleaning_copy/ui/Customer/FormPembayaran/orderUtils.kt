package com.example.comfortablecleaning_copy
import android.content.Context
import android.widget.Toast
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object OrderUtils {
    fun saveOrderToDatabase(
        context: Context,
        orderId: String,
        namaPemesan: String,
        namaProduk: String?,
        qty: Int,
        noTelpPemesan: String,
        daerahPemesan: String,
        alamatPemesan: String,
        catatanPemesan: String,
        ongkir: Int,
        totalHarga: Int,
        jenis: String?
    ) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("pesanan")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val pesanan = Pesanan(
            idPesanan = orderId,
            namaPemesan = namaPemesan,
            namaProduk = namaProduk,
            quantity = qty,
            noTelpPemesan = noTelpPemesan,
            daerahPemesan = daerahPemesan,
            alamatPemesan = alamatPemesan,
            catatanPemesan = catatanPemesan,
            ongkir = ongkir,
            totalHarga = totalHarga,
            status = "menunggu",
            jenis = jenis,
            statusPembayaran = "Success",
            userId = userId
        )

        database.child(orderId).setValue(pesanan).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Pesanan anda berhasil", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Gagal memesan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
