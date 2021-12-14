package com.galih.library.extension

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText

fun EditText.afterTextChanged(
    delay: Long,
    afterTextChanged: (String) -> Unit,
    afterDelay: (String) -> Unit
) {
    val handler = Handler(Looper.getMainLooper())
    var runnable = Runnable { }

    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
            runnable = Runnable {
                afterDelay.invoke(s.toString())
            }
            handler.postDelayed(runnable, delay)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            handler.removeCallbacks(runnable)
        }
    })
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.validate(message: String, validator: (String) -> Boolean) {
    this.afterTextChanged {
        this.error = if (validator(it)) null else message
    }
    this.error = if (validator(this.text.toString())) null else message
}

/**
 * Created by shivam on 8/7/17.
 */
fun EditText.autocapitalize() {
    filters += InputFilter.AllCaps()
}
