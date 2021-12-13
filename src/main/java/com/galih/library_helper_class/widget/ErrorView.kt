package com.galih.library_helper_class.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import com.galih.library_helper_class.R
import com.galih.library_helper_class.databinding.ErrorViewBinding
import com.galih.library_helper_class.extension.getString
import com.galih.library_helper_class.extension.gone
import com.galih.library_helper_class.extension.visible

class ErrorView : RelativeLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
    }

    private lateinit var binding: ErrorViewBinding
    private fun init() {
        gravity = Gravity.CENTER
        this.binding = ErrorViewBinding.inflate(LayoutInflater.from(this.context))
        addView(this.binding.root)
    }

    fun setIsVisible(isVisible: Boolean) {
        if (isVisible) {
            this.visible()
        } else {
            this.gone()
        }
        invalidate()
        requestLayout()
    }

    fun setView(
        message: String?,
        case: Int? = 0,
        @DrawableRes pIcon: Int? = null,
        onReload: (() -> Unit)? = null
    ) {
        this.setIsVisible(true)
        var icon = R.drawable.ic_signal_wifi_off
        this.binding.lytDisconnect.visible()
        this.binding.txtConnection.text = message
        this.binding.imgError.visible()
        this.binding.lytOffline.visible()
        when (case) {
            0 -> {
                if (message.isNullOrBlank()) this.binding.txtConnection.text =
                    getString(R.string.base_error_permission)
                icon = R.drawable.ic_block
            }
            1 -> {
                if (message.isNullOrBlank()) this.binding.txtConnection.text =
                    getString(R.string.base_error_no_gps)
                icon = R.drawable.ic_gps_off
            }
            2 -> {
                if (message.isNullOrBlank()) this.binding.txtConnection.text =
                    getString(R.string.base_error_connection)
                icon = R.drawable.ic_signal_wifi_off
            }
            4 -> {
                if (message.isNullOrBlank()) this.binding.txtConnection.text =
                    getString(R.string.base_error_no_data)
//                img_error.gone()
            }
            else -> {
                if (message.isNullOrBlank()) this.binding.txtConnection.text =
                    getString(R.string.base_error_unknown)
                icon = R.drawable.ic_bug_report
            }
        }
        this.binding.imgError.setImageResource(icon)
        pIcon?.let { this.binding.imgError.setImageResource(it) }
        this.binding.lytOffline.setOnClickListener {
            onReload?.invoke()
        }
        invalidate()
        requestLayout()
    }
}
