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
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "iceCreamId")
    val iceCreamId: Long,

    @ColumnInfo(name = "extraIds")
    val extraIds: MutableList<Long>
)


@Dao
interface ICartDAO {

    @Query("SELECT * FROM cart_table")
    fun loadAllCarts(): List<CartEntity>

//    @Query("DELETE FROM cart_table WHERE id = (:id)")
//    fun deleteCartItem(id: String)

    @Insert
    fun insertCartItem(cartEntity: CartEntity)

    @Update
    fun updateCartItem(cartEntity: CartEntity)

    @Delete
    fun deleteCartItem(cartEntity: CartEntity)

}


@Database(entities = [CartEntity::class], version = 2, exportSchema = true)
@TypeConverters(CartConverters::class)
abstract class CartDatabase: RoomDatabase() {

    abstract fun cartDao():ICartDAO
    
}

@ProvidedTypeConverter
class CartConverters{
    @TypeConverter
    fun fromLongListToStr(list: MutableList<Long>): String {
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
