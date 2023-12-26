package com.example.hortensiainventory

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class ItemsAdapter(private var items: List<Item>, private var filteredItems: List<Item>, context: Context):
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>(), Filterable {

    private  val db: ItemsDatabaseHelper = ItemsDatabaseHelper((context))

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTextView: TextView = itemView.findViewById(R.id.nameItemView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityItemView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceItemView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.items_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        // Bind data to the ViewHolder
        holder.nameTextView.text = currentItem.name
        holder.quantityTextView.text = "Quantity: " + currentItem.quantity.toString()
        holder.priceTextView.text = formatPriceAsIDR(currentItem.price)

        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateItemActivity::class.java).apply {
                putExtra("item_id", currentItem.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(currentItem, holder.itemView.context)
        }
    }

    private fun showDeleteConfirmationDialog(item: Item, context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Are you sure you want to delete '${item.name}'?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            deleteItem(item, context)
        }

        alertDialogBuilder.setNegativeButton("No") { _, _ ->
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun deleteItem(item: Item, context: Context) {
        db.deleteItem(item.id)
        updateItems(db.getAllItems())
        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
    }

    private fun formatPriceAsIDR(price: Int): String {
        val localeID = Locale("id", "ID") // Indonesia locale
        val currencyFormat = NumberFormat.getCurrencyInstance(localeID)
        return currencyFormat.format(price.toLong())
    }

    // Add a helper method to update the dataset
    fun updateItems(newItems: List<Item>) {
        items = newItems
        filteredItems = newItems // Update both lists
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val originalItems: List<Item> = items

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint.toString().lowercase(Locale.getDefault())

                val filterResults = FilterResults()

                val filteredList = originalItems.filter {
                    it.name.lowercase(Locale.getDefault()).contains(queryString)
                }

                filterResults.values = filteredList
                updateItems(filteredList)
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as? List<Item> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}