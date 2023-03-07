package com.example.grandmasicecreamkt

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.android.ext.koin.androidContext

@Entity(tableName = "cart_table")
data class CartEntity (
    @PrimaryKey
    @ColumnInfo(name = "iceCreamId")
    val iceCreamId: Long,

    @ColumnInfo(name = "extraIds")
    val extraIds: MutableList<Long>
)


@Dao
interface ICartDAO {

    @Query("SELECT * FROM cart_table")
    fun loadAllCarts(): List<CartEntity>

    @Insert
    fun insertCartItem(cartEntity: CartEntity)

    @Update
    fun updateCartItem(cartEntity: CartEntity)

    @Delete
    fun deleteCartItem(cartEntity: CartEntity)

}


@Database(entities = [CartEntity::class], version = 1, exportSchema = true)
@TypeConverters(CartConverters::class)
abstract class CartDatabase: RoomDatabase() {

    abstract fun cartDao():ICartDAO

    companion object {
        private var instance: CartDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): CartDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    ctx.applicationContext, CartDatabase::class.java,
                    "cart_database"
                )
                    .addTypeConverter(CartConverters())
                    .build()
            }
            return instance!!
        }
    }
}


class CartConverters{
    @TypeConverter
    fun fromLongListToStr(list: MutableList<String>): String {
        return list.joinToString(";")
    }

    @TypeConverter
    fun fromStrToLongList(str: String): MutableList<Long> {
        val strList = str.split(";")
        var longList = mutableListOf<Long>()
        strList.forEach{
            longList.add(it.toLong())
        }
        return longList
    }
}
