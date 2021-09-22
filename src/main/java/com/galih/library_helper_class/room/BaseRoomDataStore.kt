package com.galih.library_helper_class.room

interface BaseRoomDataStore<Entity> {
    suspend fun localAddAsync(vararg model: Entity)
    suspend fun localUpdateAsync(vararg model: Entity)
    suspend fun localDeleteAsync(vararg model: Entity)
    suspend fun localDeleteAllAsync()
}