package com.myproject.warkopgundar

import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class, Category::class, Menu::class, Cart::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun menuDao(): MenuDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db_warkopgundar"
                ).fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            val imgCoffee = R.drawable.img_menu_coffe
                            val imgMie = R.drawable.img_menu_mie
                            val imgRice = R.drawable.img_menu_rice

                            try {
                                db.execSQL("INSERT INTO categories (id, name) VALUES (1, 'Coffee')")
                                db.execSQL("INSERT INTO categories (id, name) VALUES (2, 'Mie')")
                                db.execSQL("INSERT INTO categories (id, name) VALUES (3, 'Rice')")

                                // 2. SEEDING TABEL MENUS
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Cappuccino', 'With Steamed Milk', 8000, 4.5, 12000, $imgCoffee, 1)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Espresso', 'Strong & Bold', 10000, 4.7, 8000, $imgCoffee, 1)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Coffee Latte', 'Creamy Texture', 12000, 4.6, 15000, $imgCoffee, 1)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Americano', 'Pure Black Coffee', 7000, 4.4, 10000, $imgCoffee, 1)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Moccachino', 'Coffee & Chocolate', 13000, 4.8, 9000, $imgCoffee, 1)")

                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Indomie Kuah', 'Extra Telur', 12000, 4.8, 167000, $imgMie, 2)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Indomie Goreng', 'Double Porsi', 15000, 4.9, 200000, $imgMie, 2)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Mie Nyemek', 'Pedas Level 5', 14000, 4.7, 50000, $imgMie, 2)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Mie Ayam Bakso', 'Pangsit Goreng', 18000, 4.6, 30000, $imgMie, 2)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Mie Goreng Aceh', 'Rempah Spesial', 17000, 4.5, 25000, $imgMie, 2)")

                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Nasi Goreng Warkop', 'Pake Telur Mata Sapi', 15000, 4.8, 50000, $imgRice, 3)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Nasi Telor Pontianak', 'Telur Ceplok & Kecap Gurih', 12000, 4.9, 85000, $imgRice, 3)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Magelangan', 'Mix Nasi & Mie Goreng', 17000, 4.7, 45000, $imgRice, 3)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Nasi Sarden', 'Sarden Pedas Gurih', 14000, 4.5, 20000, $imgRice, 3)")
                                db.execSQL("INSERT INTO menus (name, description, price, rating, likes, imageRes, categoryId) VALUES " +
                                        "('Nasi Kornet Telur', 'Tumis Kornet & Telur Dadar', 14000, 4.6, 30000, $imgRice, 3)")

                                android.util.Log.d("DB_SEED", "Berhasil memasukkan 3 Kategori dan 15 Menu!")
                            } catch (e: Exception) {
                                android.util.Log.e("DB_SEED", "Gagal seeding: ${e.message}")
                            }
                        }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}