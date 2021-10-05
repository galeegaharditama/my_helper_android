package com.galih.library_helper_class.room

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

interface IRoomDataStore<Entity> {
    val tableName:String
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
        Log.d("IRoomDataStore", "${rawQuery.sql} ${rawQuery.argCount}")
        return rawQuery
    }
    suspend fun localAddAsync(vararg model: Entity)
    suspend fun localUpdateAsync(vararg model: Entity)
    suspend fun localDeleteAsync(vararg model: Entity)
    suspend fun localDeleteAllAsync()
}