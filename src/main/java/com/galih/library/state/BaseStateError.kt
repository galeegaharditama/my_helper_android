package com.galih.library.state

import com.galih.library.widget.ErrorViewType

class BaseStateError(
    val exception: Throwable,
    val type: ErrorViewType = ErrorViewType.CONNECTION_PROBLEM,
    val errorMessage: String = exception.localizedMessage ?: ""
) : Throwable(errorMessage)
