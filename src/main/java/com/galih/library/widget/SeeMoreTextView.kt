package com.galih.library.widget

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import com.galih.library.R
import com.galih.library.extension.getColorCompat

/**
 * Credit to : https://github.com/AhmMhd/SeeMoreTextView-Android
 **/
class SeeMoreTextView : AppCompatTextView {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  )

  private var textMaxLength = MAX_LENGTH
  private var seeMoreTextColor = R.color.seemore_color
  private lateinit var collapsedTextWithSeeMoreButton: String
  private lateinit var expandedTextWithSeeMoreButton: String
  private lateinit var orignalContent: String

  private lateinit var collapsedTextSpannable: SpannableString
  private lateinit var expandedTextSpannable: SpannableString

  private var isExpanded = false
  private var seeMore = "See More"
  private var seeLess = "See Less"

  fun setTextMaxLength(maxLength: Int) {
    this.textMaxLength = maxLength
  }

  fun setSeeMoreTextColor(@IdRes color: Int) {
    this.seeMoreTextColor = color
  }

  fun expandText(expand: Boolean) {
    if (expand) {
      this.isExpanded = true
      text = (expandedTextSpannable)
    } else {
      isExpanded = false
      text = (collapsedTextSpannable)
    }
  }

  fun setSeeMoreText(seeMoreText: String, seeLessText: String) {
    this.seeMore = seeMoreText
    this.seeLess = seeLessText
  }

  fun isExpanded(): Boolean = this.isExpanded

  // toggle the state
  fun toggle() {
    if (isExpanded) {
      isExpanded = false
      text = (collapsedTextSpannable)
    } else {
      isExpanded = true
      text = (expandedTextSpannable)
    }
  }

  fun setContent(text: String) {
    orignalContent = text
    this.movementMethod = LinkMovementMethod.getInstance()
    // show see more
    if (orignalContent.length >= textMaxLength) {
      collapsedTextWithSeeMoreButton =
        "${orignalContent.substring(0, textMaxLength)}... $seeMore"
      expandedTextWithSeeMoreButton = "$orignalContent $seeLess"

      // creating spannable strings
      collapsedTextSpannable = SpannableString(collapsedTextWithSeeMoreButton)
      expandedTextSpannable = SpannableString(expandedTextWithSeeMoreButton)

      collapsedTextSpannable.setSpan(
        clickableSpan,
        textMaxLength + FOUR,
        collapsedTextWithSeeMoreButton.length,
        0
      )
      collapsedTextSpannable.setSpan(
        StyleSpan(Typeface.ITALIC),
        textMaxLength + FOUR,
        collapsedTextWithSeeMoreButton.length,
        0
      )
      collapsedTextSpannable.setSpan(
        RelativeSizeSpan(PROPOTION_VALUE),
        textMaxLength + FOUR,
        collapsedTextWithSeeMoreButton.length,
        0
      )
      expandedTextSpannable.setSpan(
        clickableSpan,
        orignalContent.length + ONE,
        expandedTextWithSeeMoreButton.length,
        0
      )
      expandedTextSpannable.setSpan(
        StyleSpan(Typeface.ITALIC),
        orignalContent.length + ONE,
        expandedTextWithSeeMoreButton.length,
        0
      )
      expandedTextSpannable.setSpan(
        RelativeSizeSpan(PROPOTION_VALUE),
        orignalContent.length + ONE,
        expandedTextWithSeeMoreButton.length,
        0
      )

      if (isExpanded) {
        setText(expandedTextSpannable)
      } else {
        setText(collapsedTextSpannable)
      }
    } else {
      // to do: don't show see more
      setText(orignalContent)
    }
  }

  private val clickableSpan = object : ClickableSpan() {
    override fun onClick(widget: View) {
      toggle()
    }

    override fun updateDrawState(ds: TextPaint) {
      super.updateDrawState(ds)
      ds.isUnderlineText = false
      ds.color = context.getColorCompat(seeMoreTextColor)
    }
  }

  companion object {
    private const val MAX_LENGTH = 100
    private const val FOUR = 4
    private const val ONE = 1
    private const val PROPOTION_VALUE = .9f
  }
}
