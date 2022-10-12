package com.galih.library.room

import androidx.room.ColumnInfo

open class BaseEntity {
  @ColumnInfo(name = "status")
  var status: Int = 1

  @ColumnInfo(name = "status_kirim")
  var statusKirim: StatusKirim = StatusKirim.NOT_SENT

  @ColumnInfo(name = "created_at")
  var createdAt: String = ""

  @ColumnInfo(name = "updated_at")
  var updatedAt: String = ""
}
