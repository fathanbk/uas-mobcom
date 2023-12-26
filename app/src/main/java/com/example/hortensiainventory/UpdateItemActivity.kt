package com.example.hortensiainventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.hortensiainventory.databinding.ActivityAddItemBinding
import com.example.hortensiainventory.databinding.ActivityUpdateItemBinding

class UpdateItemActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateItemBinding
    private lateinit var db : ItemsDatabaseHelper
    private var itemId: Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ItemsDatabaseHelper(this)

        itemId = intent.getIntExtra("item_id",-1)
        if(itemId == -1){
            finish()
            return
        }

        val item = db.getItemById(itemId)
        binding.nameItemEdit.setText(item.name)
        binding.quantityItemEdit.setText(item.quantity.toString())
        binding.priceItemEdit.setText(item.price.toString())

        binding.editSaveButton.setOnClickListener{
            val newName = binding.nameItemEdit.text.toString()
            val newQuantity = binding.quantityItemEdit.text.toString().toInt()
            val newPrice = binding.priceItemEdit.text.toString().toInt()
            val updatedItem = Item(itemId,newName,newQuantity,newPrice)
            db.updateItem(updatedItem)
            finish()
            Toast.makeText(this, "Item Updated!", Toast.LENGTH_SHORT).show()

        }
        binding.priceItemEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                // The user pressed the Enter key or Done action

                // Check if all necessary EditText fields are filled
                val name = binding.nameItemEdit.text.toString()
                val quantity = binding.quantityItemEdit.text.toString()
                val price = binding.priceItemEdit.text.toString()

                if (name.isNotBlank() && quantity.isNotBlank() && price.isNotBlank()) {
                    // All fields are filled, proceed with saving
                    val item = Item(itemId, name, quantity.toInt(), price.toInt())
                    db.updateItem(item)
                    finish()
                    Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show()
                    // Optionally, you can clear the fields after saving
                    binding.nameItemEdit.text.clear()
                    binding.quantityItemEdit.text.clear()
                    binding.priceItemEdit.text.clear()
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