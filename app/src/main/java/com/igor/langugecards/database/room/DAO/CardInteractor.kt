package com.igor.langugecards.database.room.DAO

import androidx.room.*
import com.igor.langugecards.model.Card
import io.reactivex.Observable

@Dao
interface CardInteractor {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCard(card: Card)

    @Delete
    fun deleteCard(card: Card)

    @Query(value = "DELETE FROM Card WHERE mId == :cardId")
    fun deleteCardById(cardId: Long)

    @Query(value = "SELECT * FROM Card")
    fun getAllCards(): Observable<MutableList<Card>>

    @Query(value = "SELECT * FROM Card WHERE mTheme == :theme")
    fun getCardsByTheme(theme: String): Observable<List<Card>>

    @Query(value = "SELECT * FROM Card WHERE mId == :id")
    fun getCardById(id: Long): Observable<Card>
}