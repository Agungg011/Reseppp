package com.example.reseppp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class DetailActivity : AppCompatActivity() {

    private val supabase = createSupabaseClient(
        supabaseUrl = "https://qwaxgqzndcamdtlenohn.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF3YXhncXpuZGNhbWR0bGVub2huIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU1Njg4MjgsImV4cCI6MjA2MTE0NDgyOH0.ixow9jFeCWUe0heOx4N2a1vSUqf-OtnPR5SluTXyYUM"
    ) {
        install(Postgrest)
    }

    @Serializable
    data class ResepMakan(
        val id: Int,
        val gambar: String,
        @SerialName("nama_resep") val namaResep: String,
        val alat_dan_bahan: String,
        val langkah: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val resepId = intent.getIntExtra("resep_id", -1)
        if (resepId != -1) {
            fetchDetailResep(resepId)
        }
    }

    private fun fetchDetailResep(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = supabase.from("resepmakan")
                    .select {
                        filter {
                            eq("id", id)
                        }
                    }
                    .decodeSingle<ResepMakan>()

                withContext(Dispatchers.Main) {
                    val image = findViewById<ImageView>(R.id.imageDetailResep)
                    val namaResep = findViewById<TextView>(R.id.textNamaResep)
                    val alatBahan = findViewById<TextView>(R.id.textDetailAlatDanBahan)
                    val langkah = findViewById<TextView>(R.id.textDetailLangkah)

                    Glide.with(this@DetailActivity).load(response.gambar).into(image)
                    namaResep.text = response.namaResep
                    alatBahan.text = response.alat_dan_bahan
                    langkah.text = response.langkah
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Tampilkan pesan error jika perlu
            }
        }
    }
}
