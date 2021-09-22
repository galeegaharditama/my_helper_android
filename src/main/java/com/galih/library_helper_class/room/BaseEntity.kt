package com.galih.library_helper_class.room

import androidx.room.ColumnInfo

abstract class BaseEntity {
    @ColumnInfo(name = "status")
    var status: Int = 1
    @ColumnInfo(name = "status_kirim")
    var status_kirim: StatusKirim = StatusKirim.NOT_SENT
    @ColumnInfo(name = "created_at")
    var created_at: String = ""
    @ColumnInfo(name = "updated_at")
    var updated_at: String = ""
}