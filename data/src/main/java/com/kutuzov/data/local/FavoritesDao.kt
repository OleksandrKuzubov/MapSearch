package com.kutuzov.data.local

import androidx.room.*
import com.kutuzov.data.entities.AddressDto

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    suspend fun getFavorites() : List<AddressDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AddressDto)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun removeAddressById(id: String)
}