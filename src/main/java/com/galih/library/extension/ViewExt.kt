package com.galih.library.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.reduceDragSensitivity(value: Int = 5) {
  val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
  recyclerViewField.isAccessible = true
  val recyclerView = recyclerViewField.get(this) as RecyclerView

  val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
  touchSlopField.isAccessible = true
  val touchSlop = touchSlopField.get(recyclerView) as Int
//    touchSlopField.set(recyclerView, touchSlop*8) // "8" was obtained experimentally
  touchSlopField.set(recyclerView, touchSlop * value)
}

fun getScreenHeight(): Int {
  return Resources.getSystem().displayMetrics.heightPixels
}

/**
 * Method used to easily retrieve display size from [View].
 */
fun View.getDisplaySize() = context.getDisplaySize()

/**
 * Extension method to provide show keyboard for View.
 */
fun View.showKeyboard() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  this.requestFocus()
  imm.showSoftInput(this, 0)
}

/**
 * Extension method to provide hide keyboard for [View].
 */
fun View.hideKeyboard() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSoftKeyboard() {
  val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun View.hideSoftKeyboard() {
  val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun View.getString(stringResId: Int): String = resources.getString(stringResId)

fun View.gone() {
  if (visibility != View.GONE) {
    visibility = View.GONE
  }
}

fun View.visible() {
  if (visibility != View.VISIBLE) {
    visibility = View.VISIBLE
  }
}

fun View.invisible() {
  if (visibility != View.INVISIBLE) {
    visibility = View.INVISIBLE
  }
}

fun View.getLayoutInflater() = context.getLayoutInflater()

fun View.buttonDisabledClick(@ColorRes colorDisable: Int) {
  this.isEnabled = false
  this.setBackgroundColor(ContextCompat.getColor(this.context, colorDisable))
}

fun View.buttonEnabledClick(@ColorRes colorEnable: Int) {
  this.isEnabled = true
  this.setBackgroundColor(ContextCompat.getColor(this.context, colorEnable))
}

fun View.expand(onFinishListener: (() -> Unit)? = null) {
  val a = expandAction(this)
  a.setAnimationListener(object : Animation.AnimationListener {
    override fun onAnimationStart(p0: Animation?) {
      TODO("Not yet implemented")
    }

    override fun onAnimationEnd(p0: Animation?) {
      onFinishListener?.invoke()
    }

    override fun onAnimationRepeat(p0: Animation?) {
      TODO("Not yet implemented")
    }
  })
  this.startAnimation(a)
}

private fun expandAction(v: View): Animation {
  v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  val targtetHeight = v.measuredHeight
  v.layoutParams.height = 0
  v.visibility = View.VISIBLE
  val a: Animation = object : Animation() {
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
      v.layoutParams.height =
        if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targtetHeight * interpolatedTime).toInt()
      v.requestLayout()
    }

    override fun willChangeBounds(): Boolean {
      return true
    }
  }
  a.duration = (targtetHeight / v.context.resources.displayMetrics.density).toLong()
  v.startAnimation(a)
  return a
}

fun View.collapse() {
  val initialHeight = this.measuredHeight
  val a: Animation = object : Animation() {
    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
      if (interpolatedTime == 1f) {
        this@collapse.gone()
      } else {
        this@collapse.layoutParams.height =
          initialHeight - (initialHeight * interpolatedTime).toInt()
        this@collapse.requestLayout()
      }
    }

    override fun willChangeBounds(): Boolean {
      return true
    }
  }
  a.duration = ((initialHeight / this.context.resources.displayMetrics.density).toLong())
  this.startAnimation(a)
}

fun View.flyInDown(duration: Long = 200, onFinishListener: (() -> Unit)? = null) {
  this.visible()
  this.alpha = 0.0f
  this.translationY = 0f
  this.translationY = -this.height.toFloat()
  // Prepare the View for the animation
  this.animate()
    .setDuration(duration)
    .translationY(0f)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator?) {
        onFinishListener?.invoke()
        super.onAnimationEnd(animation)
      }
    })
    .alpha(1.0f)
    .start()
}

fun View.flyOutDown(duration: Long = 200, onFinishListener: (() -> Unit)? = null) {
  this.visibility = View.VISIBLE
  this.alpha = 1.0f
  this.translationY = 0f
  // Prepare the View for the animation
  this.animate()
    .setDuration(duration)
    .translationY(this.height.toFloat())
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator?) {
        onFinishListener?.invoke()
        super.onAnimationEnd(animation)
      }
    })
    .alpha(0.0f)
    .start()
}

fun View.fadeIn(duration: Long = 200, onFinishListener: (() -> Unit)? = null) {
  this.gone()
  this.alpha = 0.0f
  // Prepare the View for the animation
  this.animate()
    .setDuration(duration)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        this@fadeIn.visible()
        onFinishListener?.invoke()
        super.onAnimationEnd(animation)
      }
    })
    .alpha(1.0f)
}

fun View.fadeOut(duration: Long = 500, onFinishListener: (() -> Unit)? = null) {
  this.alpha = 1.0f
  // Prepare the View for the animation
  this.animate()
    .setDuration(duration)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        onFinishListener?.invoke()
        super.onAnimationEnd(animation)
      }
    })
    .alpha(0.0f)
}

fun View.showIn(duration: Long = 200) {
  this.visible()
  this.alpha = 0f
  this.translationY = this.height.toFloat()
  this.animate()
    .setDuration(duration)
    .translationY(0f)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        super.onAnimationEnd(animation)
      }
    })
    .alpha(1f)
    .start()
}

fun View.initShowOut() {
  this.gone()
  this.translationY = this.height.toFloat()
  this.alpha = 0f
}

fun View.showOut(duration: Long = 200) {
  this.visible()
  this.alpha = 1f
  this.translationY = 0f
  this.animate()
    .setDuration(duration)
    .translationY(this.height.toFloat())
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        this@showOut.gone()
        super.onAnimationEnd(animation)
      }
    }).alpha(0f)
    .start()
}

private const val CLOCKWISE = 135f
fun View.rotateFab(duration: Long = 200, rotate: Boolean): Boolean {
  this.animate().setDuration(duration)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        super.onAnimationEnd(animation)
      }
    })
    .rotation(if (rotate) CLOCKWISE else 0f)
  return rotate
}

@Suppress("MagicNumber")
fun View.fadeOutIn(duration: Long = 500) {
  this.alpha = 0f
  val animatorSet = AnimatorSet()
  val animatorAlpha: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 0.5f, 1f)
  ObjectAnimator.ofFloat(this, "alpha", 0f).start()
  animatorAlpha.duration = duration
  animatorSet.play(animatorAlpha)
  animatorSet.start()
}

fun View.showScale(duration: Long = 200, onFinishListener: (() -> Unit)? = null) {
  this.animate()
    .scaleY(1f)
    .scaleX(1f)
    .setDuration(duration)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        onFinishListener?.invoke()
        super.onAnimationEnd(animation)
      }
    })
    .start()
}

fun View.hideScale(duration: Long = 200, onFinishListener: (() -> Unit)? = null) {
  this.animate()
    .scaleY(0f)
    .scaleX(0f)
    .setDuration(duration)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        onFinishListener?.invoke()
        super.onAnimationEnd(animation)
      }
    })
    .start()
}

fun View.hideFab(duration: Long = 300) {
  val moveY = 2 * this.height
  this.animate()
    .translationY(moveY.toFloat())
    .setDuration(duration)
    .start()
}

fun View.showFab(duration: Long = 300) {
  this.animate()
    .translationY(0f)
    .setDuration(duration)
    .start()
}
