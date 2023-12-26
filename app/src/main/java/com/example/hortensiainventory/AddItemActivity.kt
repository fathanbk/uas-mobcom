package com.example.hortensiainventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.hortensiainventory.databinding.ActivityAddItemBinding
import com.example.hortensiainventory.databinding.ActivityMainBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private lateinit var db: ItemsDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ItemsDatabaseHelper(this)

        binding.saveAddButton.setOnClickListener{
            val name = binding.nameItemAdd.text.toString()
            val quantity = binding.quantityItemAdd.text.toString().toInt()
            val price = binding.priceItemAdd.text.toString().toInt()
            val item = Item(0, name, quantity, price)
            db.insertItem(item)
            finish()
            Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show()
        }

        binding.priceItemAdd.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                // The user pressed the Enter key or Done action

                // Check if all necessary EditText fields are filled
                val name = binding.nameItemAdd.text.toString()
                val quantity = binding.quantityItemAdd.text.toString()
                val price = binding.priceItemAdd.text.toString()

                if (name.isNotBlank() && quantity.isNotBlank() && price.isNotBlank()) {
                    // All fields are filled, proceed with saving
                    val item = Item(0, name, quantity.toInt(), price.toInt())
                    db.insertItem(item)
                    finish()
                    Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show()
                    // Optionally, you can clear the fields after saving
                    binding.nameItemAdd.text.clear()
                    binding.quantityItemAdd.text.clear()
                    binding.priceItemAdd.text.clear()
                }

                // Return true to indicate that the action has been handled
                true
            } else {
                // Return false to let the system handle the action
                false
            }
        }
    }
}