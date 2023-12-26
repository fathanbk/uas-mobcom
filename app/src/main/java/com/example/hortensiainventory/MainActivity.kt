package com.example.hortensiainventory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hortensiainventory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: ItemsDatabaseHelper
    private lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        db = ItemsDatabaseHelper(this)
        itemsAdapter= ItemsAdapter(db.getAllItems(),db.getAllItems(), this)

        binding.itemList.layoutManager = LinearLayoutManager(this)
        binding.itemList.adapter= itemsAdapter

        binding.addButton.setOnClickListener{
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        val searchView = findViewById<SearchView>(R.id.searchView)

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.onActionViewExpanded()
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    searchView.isIconified = true
                    searchView.clearFocus()
                    return false
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    itemsAdapter.updateItems(db.getAllItems())
                    searchView.isIconified = true
                    searchView.clearFocus()
                } else {
                    searchView.isIconified = false
                    binding.searchView.onActionViewExpanded()
                    itemsAdapter.updateItems(db.getAllItems())
                    itemsAdapter.filter.filter(newText)
                }
                return true
            }
        })

    }

    override fun onResume(){
        super.onResume()
        itemsAdapter.updateItems(db.getAllItems())
        binding.searchView.isIconified = true
        binding.searchView.clearFocus()
    }
}