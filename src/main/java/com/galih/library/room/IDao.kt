package com.galih.library.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.galih.library.extension.toDateTime

// DON'T USE THIS
interface IDaoOld<T : BaseEntity> {

    val formatDateTime: String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: T)

    suspend fun insert(vararg models: T): List<T> {
        return models.map {
            this.insertWithTimeStamp(it, formatDateTime)
        }
    }

    private suspend fun insertWithTimeStamp(model: T, formatDateTime: String?): T {
        val dateTime = formatDateTime?.let {
            System.currentTimeMillis().toDateTime(it)
        } ?: System.currentTimeMillis().toString()
        model.createdAt = dateTime
        model.updatedAt = dateTime
        this.insert(model)
        return model
    }

    @Update
    suspend fun update(model: T)

    suspend fun update(vararg models: T): List<T> {
        return models.map {
            this.updateWithTimeStamp(it, formatDateTime)
        }
    }

    private suspend fun updateWithTimeStamp(model: T, formatDateTime: String?): T {
        val dateTime = formatDateTime?.let {
            System.currentTimeMillis().toDateTime(it)
        } ?: System.currentTimeMillis().toString()
        model.updatedAt = dateTime
        this.update(model)
        return model
    }

    @Delete
    suspend fun delete(model: T)

    suspend fun safeDelete(model: T) {
        model.status = 0
        this.updateWithTimeStamp(model, formatDateTime)
    }
}

abstract class IDao<T : BaseEntity>() {
    private var formatDateTime: String? = null
    constructor(pattern: String) : this() {
        this.formatDateTime = pattern
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(model: T)

    suspend fun insert(vararg models: T): List<T> {
        return models.map {
            this.insertWithTimeStamp(it, formatDateTime)
        }
    }

    private suspend fun insertWithTimeStamp(model: T, formatDateTime: String?): T {
        val dateTime = formatDateTime?.let {
            System.currentTimeMillis().toDateTime(it)
        } ?: System.currentTimeMillis().toString()
        model.createdAt = dateTime
        model.updatedAt = dateTime
        this.insert(model)
        return model
    }

    @Update
    abstract suspend fun update(model: T)

    suspend fun update(vararg models: T): List<T> {
        return models.map {
            this.updateWithTimeStamp(it, formatDateTime)
        }
    }

    private suspend fun updateWithTimeStamp(model: T, formatDateTime: String?): T {
        val dateTime = formatDateTime?.let {
            System.currentTimeMillis().toDateTime(it)
        } ?: System.currentTimeMillis().toString()
        model.updatedAt = dateTime
        this.update(model)
        return model
    }

    @Delete
    abstract suspend fun delete(model: T)

    suspend fun safeDelete(model: T) {
        model.status = 0
        this.updateWithTimeStamp(model, formatDateTime)
    }
}
