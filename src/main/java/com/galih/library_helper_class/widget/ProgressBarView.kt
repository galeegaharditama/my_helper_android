package com.galih.library_helper_class.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.galih.library_helper_class.R
import com.galih.library_helper_class.databinding.LoadingViewBinding
import com.galih.library_helper_class.extension.getColorCompat
import com.galih.library_helper_class.extension.getString
import com.galih.library_helper_class.extension.gone
import com.galih.library_helper_class.extension.visible

class ProgressBarView : RelativeLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        initAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        initAttributes(context, attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
        initAttributes(context, attrs, defStyleAttr)
    }

    private lateinit var binding: LoadingViewBinding
    private fun init() {
        gravity = Gravity.CENTER
        this.binding = LoadingViewBinding.inflate(LayoutInflater.from(context))
        addView(this.binding.root)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) {
        val attributes =
            context.obtainStyledAttributes(attrs, R.styleable.ProgressBarView, defStyleAttr, 0)
        val isShowTextLoading = attributes.getBoolean(R.styleable.ProgressBarView_isShowText, true)

        if (isShowTextLoading) this.binding.textLoading.visible()
        else this.binding.textLoading.gone()

        this.binding.textLoading.text = attributes.getString(R.styleable.ProgressBarView_textValue)
        this.binding.textLoading.setTextColor(
            attributes.getColor(
                R.styleable.ProgressBarView_textColor,
                context.getColorCompat(R.color.grey_800)
            )
        )
        attributes.recycle()
    }

    fun setLoading(textLoading: String? = null) {
        this.setIsVisible(true)
        var message = getString(R.string.base_loading)
        if (!textLoading.isNullOrBlank()) {
            message = textLoading
        }
        this.binding.textLoading.text = message
        invalidate()
        requestLayout()
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

}