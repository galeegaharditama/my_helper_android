package com.galih.library.room

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import timber.log.Timber

interface IRoomDataStore<Entity> {
    val tableName: String
    fun setQuery(
        selectQuery: String = "SELECT a.* ",
        joinQuery: String = "",
        whereQuery: String = "",
        orderByQuery: String = "",
        groupQuery: String = "",
        args: MutableList<Any> = mutableListOf()
    ): SupportSQLiteQuery {
        val baseQuery = "$selectQuery FROM ${this.tableName} a"
        val rawQuery = SimpleSQLiteQuery(
            "$baseQuery $joinQuery $whereQuery $groupQuery $orderByQuery",
            args.toTypedArray()
        )
        Timber.d(rawQuery.sql + " " + rawQuery.argCount)
        return rawQuery
    }
    suspend fun localAddAsync(vararg model: Entity)
    suspend fun localUpdateAsync(vararg model: Entity)
    suspend fun localDeleteAsync(vararg model: Entity)
    suspend fun localDeleteAllAsync()
}
