package com.kutuzov.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kutuzov.data.entities.AddressDto

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    suspend fun getFavorites() : List<AddressDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AddressDto)
}