package com.myproject.warkopgundar.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.features.dashboard.fragments.carts.Cart
import com.myproject.warkopgundar.features.dashboard.fragments.carts.CartDao

@Database(entities = [User::class, Category::class, Menu::class, Cart::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun menuDao(): MenuDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db_warkopgundar"
                ).fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            try {
                                db.execSQL("INSERT INTO categories (id, name) VALUES (1, 'Coffee')")
                                db.execSQL("INSERT INTO categories (id, name) VALUES (2, 'Mie')")
                                db.execSQL("INSERT INTO categories (id, name) VALUES (3, 'Rice')")

                                // 2. SEEDING TABEL MENUS
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Kopi Hitam','Kopi tubruk panas',5000,4.5,25000,${R.drawable.img_menu_coffee_hitam},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Kopi Susu','Kopi hitam + susu kental manis',6000,4.6,30000,${R.drawable.img_menu_coffee_susu},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Kopi Jahe','Hangat & pedas jahe',7000,4.4,15000,${R.drawable.img_menu_coffee_jahe},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Kopi ABC','Kopi sachet favorit',5000,4.3,20000,${R.drawable.img_menu_coffee_abc},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Kopi Kapal Api','Kopi sachet legendaris',5000,4.5,40000,${R.drawable.img_menu_coffee_kapalapi},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Kopi Good Day','Manis & ringan',6000,4.4,18000,${R.drawable.img_menu_coffee_gooday},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Teh Manis Panas','Teh panas manis',4000,4.2,12000,${R.drawable.img_menu_coffee_tehmanispanas},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Teh Manis Dingin','Teh dingin segar',5000,4.3,15000,${R.drawable.img_menu_coffee_tehmanisdingin},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Susu Jahe','Susu hangat jahe',7000,4.4,14000,${R.drawable.img_menu_coffee_susujahe},1)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Susu Coklat','Susu coklat hangat',6000,4.5,16000,${R.drawable.img_menu_coffee_susucoklat},1)")

                                db.execSQL("INSERT INTO menus VALUES (NULL,'Indomie Goreng','Telur ceplok',12000,4.8,120000,${R.drawable.img_menu_noodle_indomiegorengtelorceplok},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Indomie Kuah','Telur + sayur',11000,4.7,90000,${R.drawable.img_menu_noodle_indomiekuah},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Indomie Goreng Double','Porsi dobel',15000,4.9,150000,${R.drawable.img_menu_noodle_indomiegorengdouble},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Mie Nyemek','Setengah kuah pedas',14000,4.8,70000,${R.drawable.img_menu_noodle_mienyemek},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Mie Rebus Telur','Kuah gurih',12000,4.6,50000,${R.drawable.img_menu_noodle_mierebustelur},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Mie Goreng Telur','Telur dadar',13000,4.7,65000,${R.drawable.img_menu_noodle_miegorengtelur},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Mie Goreng Kornet','Kornet sapi',15000,4.6,45000,${R.drawable.img_menu_noodle_miegorengkornet},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Mie Goreng Sosis','Sosis goreng',15000,4.5,40000,${R.drawable.img_menu_noodle_miegorengsosis},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Mie Kuah Pedas','Cabe rawit',13000,4.7,60000,${R.drawable.img_menu_noodle_miekuahpedas},2)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Mie Goreng Spesial','Telur + sosis + kornet',18000,4.9,100000,${R.drawable.img_menu_noodle_miegorengspesial},2)")

                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Goreng','Nasi goreng warkop',14000,4.7,80000,${R.drawable.img_menu_rice_nasigoreng},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Goreng Telur','Telur ceplok',15000,4.8,90000,${R.drawable.img_menu_rice_nasigorengtelur},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Goreng Sosis','Sosis iris',16000,4.6,50000,${R.drawable.img_menu_rice_nasigorengsosis},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Goreng Kornet','Kornet sapi',17000,4.7,60000,${R.drawable.img_menu_rice_nasigorengkornet},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Telur Ceplok','Telur ceplok + kecap',10000,4.5,70000,${R.drawable.img_menu_rice_nasitelurceplok},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Telur Dadar','Telur dadar gurih',11000,4.6,65000,${R.drawable.img_menu_rice_nasitelurdadar},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Ayam Suwir','Ayam suwir sederhana',18000,4.7,45000,${R.drawable.img_menu_rice_nasiayamsuwir},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Sarden','Sarden pedas',13000,4.6,40000,${R.drawable.img_menu_rice_nasisarden},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Kornet','Kornet tumis',13000,4.5,35000,${R.drawable.img_menu_rice_nasikornet},3)")
                                db.execSQL("INSERT INTO menus VALUES (NULL,'Nasi Spesial Warkop','Telur + sosis + kornet',20000,4.9,120000,${R.drawable.img_menu_rice_nasispesial},3)")

                                Log.d("DB_SEED", "Berhasil memasukkan 3 Kategori dan 15 Menu!")
                            } catch (e: Exception) {
                                Log.e("DB_SEED", "Gagal seeding: ${e.message}")
                            }
                        }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}