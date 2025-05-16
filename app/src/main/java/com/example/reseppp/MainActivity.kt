package com.example.reseppp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var resepRecyclerView: RecyclerView
    private lateinit var resepAdapter: ResepAdapter
    private val resepList = mutableListOf<ResepMakan>()
    private lateinit var searchField: EditText
    private lateinit var searchButton: Button

    private val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://qwaxgqzndcamdtlenohn.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF3YXhncXpuZGNhbWR0bGVub2huIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU1Njg4MjgsImV4cCI6MjA2MTE0NDgyOH0.ixow9jFeCWUe0heOx4N2a1vSUqf-OtnPR5SluTXyYUM"
    ) {

        install(Postgrest)
        //install other modules
    }
    @Serializable
    data class ResepMakan(
        val id: Int,
        val gambar: String,
        @SerialName("nama_resep")
        val namaResep: String,

        val alat_dan_bahan: String = "",
        val langkah: String = ""
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        resepRecyclerView = findViewById(R.id.resepRecyclerView)
        resepRecyclerView.layoutManager = LinearLayoutManager(this)
        resepAdapter = ResepAdapter(resepList)
        resepRecyclerView.adapter = resepAdapter

        searchField = findViewById(R.id.searchResep)
        searchButton = findViewById(R.id.buttonSearch)

        fetchResepMakan()

        searchButton.setOnClickListener {
            val keyword = searchField.text.toString().trim()
            if (keyword.isEmpty()) {
                fetchResepMakan()
            } else {
                val filteredList = resepList.filter {
                    it.namaResep.contains(keyword, ignoreCase = true)
                }
                resepAdapter.updateData(filteredList)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchResepMakan() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = supabase.from("resepmakan")
                    .select(columns = Columns.list("id", "gambar", "nama_resep", "alat_dan_bahan", "langkah"))
                    .decodeList<ResepMakan>()

                withContext(Dispatchers.Main) {
                    resepList.clear()
                    resepList.addAll(data)
                    resepAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Tambahkan penanganan error sesuai kebutuhan
            }
        }
    }
}

