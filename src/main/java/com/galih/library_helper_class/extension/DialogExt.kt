package com.galih.library_helper_class.extension

import android.app.Dialog
import android.view.WindowManager

fun Dialog.hideSoftKeyboard() {
    this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}
