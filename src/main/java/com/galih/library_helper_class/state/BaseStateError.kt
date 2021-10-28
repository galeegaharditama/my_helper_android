package com.galih.library_helper_class.state

/**
 * @param case
 * 0 = Masalah Pada Permission, 1 = GPS Tidak Aktif, 2 = Koneksi Internet Bermasalah,
 * 3 = Error Bugs, 4 = Tidak Ada Data, else = Unknown Error ()
 *
 **/
class BaseStateError(
    val exception: Throwable,
    val case: Int = 4,
    val message: String = exception.localizedMessage ?: ""
)
