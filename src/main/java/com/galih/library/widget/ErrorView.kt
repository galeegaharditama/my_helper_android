package com.galih.library.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import com.galih.library.R
import com.galih.library.extension.getString
import com.galih.library.extension.gone
import com.galih.library.extension.visible
import com.galih.library_helper_class.databinding.ErrorViewBinding

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
        errorType: ErrorViewType? = ErrorViewType.PERMISSION,
        @DrawableRes pIcon: Int? = null,
        onReload: (() -> Unit)? = null
    ) {
        this.setIsVisible(true)
        var icon = R.drawable.ic_signal_wifi_off
        this.binding.lytDisconnect.visible()
        this.binding.txtConnection.text = message
        this.binding.imgError.visible()
        this.binding.lytOffline.visible()
        when (errorType) {
            ErrorViewType.PERMISSION -> {
                if (message.isNullOrBlank()) this.binding.txtConnection.text =
                    getString(R.string.base_error_permission)
                icon = R.drawable.ic_block
            }
            ErrorViewType.GPS_INACTIVE -> {
                if (message.isNullOrBlank()) this.binding.txtConnection.text =
                    getString(R.string.base_error_no_gps)
                icon = R.drawable.ic_gps_off
            }
            ErrorViewType.CONNECTION_PROBLEM -> {
                if (message.isNullOrBlank()) this.binding.txtConnection.text =
                    getString(R.string.base_error_connection)
                icon = R.drawable.ic_signal_wifi_off
            }
            ErrorViewType.DATA_EMPTY -> {
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

enum class ErrorViewType {
    PERMISSION, GPS_INACTIVE, CONNECTION_PROBLEM, DATA_EMPTY
}
