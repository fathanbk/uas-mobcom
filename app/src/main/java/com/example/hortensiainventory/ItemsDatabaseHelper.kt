package com.example.hortensiainventory

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ItemsDatabaseHelper (context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "hortensiainventory.db"
        private const val DATABASE_VERSION = 4
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_QUANTITY = "quantity"
        private const val COLUMN_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createItemsTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_QUANTITY INTEGER,"
                + "$COLUMN_PRICE INTEGER)"
                )
        db?.execSQL(createItemsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Upgrade the database schema from version 1 to version 2
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_PRICE INTEGER")
        }
        // Add more upgrade logic as needed for future versions
        val dropTableExist = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableExist)
        onCreate(db)
    }

    fun insertItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, item.name)
            put(COLUMN_QUANTITY, item.quantity)
            put(COLUMN_PRICE, item.price)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllItems():List<Item>{
        val itemsList = mutableListOf<Item>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val quantity= cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))

            val item = Item(id,name,quantity,price)
            itemsList.add(item)
        }
        cursor.close()
        db.close()
        return itemsList
    }

    fun updateItem(item:Item){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, item.name)
            put(COLUMN_QUANTITY, item.quantity)
            put(COLUMN_PRICE, item.price)
        }
        val whereClause ="$COLUMN_ID = ?"
        val whereArgs = arrayOf(item.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getItemById(itemId: Int):Item{
        val db= readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $itemId"
        val cursor =db.rawQuery(query,null)
        cursor.moveToFirst()

        val id =cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val quantity= cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
        val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))

        cursor.close()
        db.close()
        return Item(id, name, quantity, price)

    }

    fun deleteItem(itemId: Int){
        val db = writableDatabase
        val whereClause ="$COLUMN_ID = ?"
        val whereArgs = arrayOf(itemId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}