package com.example.comfortablecleaning_copy.Customer.ListCleaningShoes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.comfortablecleaning_copy.Customer.Beranda.BerandaFragment
import com.example.comfortablecleaning_copy.Customer.DetailCleaning.DetailCleaningActivity
import com.example.comfortablecleaning_copy.R

class ListCleaningShoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_cleaning_shoes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //belum fix
        val backButton: ImageView = findViewById(R.id.iv_back)
        backButton.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            val fragment = BerandaFragment()
            transaction.replace(R.id.frame_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val item1 : LinearLayout = findViewById(R.id.item1)
        val item2 : LinearLayout = findViewById(R.id.item2)
        val item3 : LinearLayout = findViewById(R.id.item3)
        val listener = View.OnClickListener { view ->
            val intent = Intent(this, DetailCleaningActivity::class.java)
            when (view.id) {
                R.id.item1 -> intent.putExtra("ITEM_CLICKED", "Item 1")
                R.id.item2 -> intent.putExtra("ITEM_CLICKED", "Item 2")
                R.id.item3 -> intent.putExtra("ITEM_CLICKED", "Item 3")
            }
            startActivity(intent)
        }
        item1.setOnClickListener(listener)
        item2.setOnClickListener(listener)
        item3.setOnClickListener(listener)

    }
}