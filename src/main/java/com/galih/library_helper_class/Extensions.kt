package com.galih.library_helper_class

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Adds variable placeholders () to the given string. Each placeholder is separated
 * by a comma.
 *
 * @param symbol => symbol to append this string
 * @param count => Number of PlaceHolder
 * to APPEND symbol (?) to String
 **/
fun StringBuilder.appendPlaceHolder(
    delimiter: String = ",",
    symbol: String,
    count: Int
): StringBuilder {
    for (i in 0 until count) {
        this.append(symbol)
        if (i < count - 1) {
            this.append(delimiter)
        }
    }
    return this
}

fun String.ucWords(): String {
    return this.lowercase(Locale.getDefault()).split(' ').joinToString(" ") {
        it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

fun Long.toDateTime(pattern: String): String {
    val newFormat = SimpleDateFormat(pattern, Locale.US)
    return newFormat.format(Date(this))
}

fun String.toTimeMillis(pattern: String = "yyyy-MM-dd HH:mm:ss"): Long {
    return SimpleDateFormat(pattern, Locale.US).parse(this)?.time
        ?: throw NullPointerException("Parse Return Null")
}

fun Long.plus(days: Int = 0, weeks: Int = 0): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.WEEK_OF_YEAR, weeks)
    calendar.add(Calendar.DAY_OF_YEAR, days)
    return calendar.timeInMillis
}

fun View.getString(stringResId: Int): String = resources.getString(stringResId)

fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

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

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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

fun TextInputLayout.validate(message: String, validator: () -> Boolean) {
    this.error = if (validator()) null else message
}

/**
 * Created by shivam on 8/7/17.
 */
fun EditText.autocapitalize() {
    filters += InputFilter.AllCaps()
}

/**
 * Method used to easily retrieve [WindowManager] from [Context].
 */
fun Context.getWindowManager() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

/**
 * Method used to easily retrieve display size from [View].
 */
fun View.getDisplaySize() = context.getDisplaySize()

/**
 * Method used to easily retrieve display size from [Context].
 */
fun Context.getDisplaySize() = Point().apply {
    getWindowManager().defaultDisplay.getSize(this)
}


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
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.hideSoftKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    } else {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

/**
 * Return whether Keyboard is currently visible on screen or not.
 *
 * @return true if keyboard is visible.
 */
fun Activity.isKeyboardVisible(): Boolean {
    val r = Rect()

    //r will be populated with the coordinates of your view that area still visible.
    window.decorView.getWindowVisibleDisplayFrame(r)

    //get screen height and calculate the difference with the usable area from the r
    val height = getDisplaySize().y
    val diff = height - r.bottom

    // If the difference is not 0 we assume that the keyboard is currently visible.
    return diff != 0
}
/** END ACTIVITY EXTENSION */

/** BEGIN FRAGMENT EXTENSION */
/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}

fun Dialog.hideSoftKeyboard() {
    this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

fun View.getLayoutInflater() = context.getLayoutInflater()

fun Context.getLayoutInflater() =
    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

fun Context.dpToPx(dp: Int): Int {
    val resource = this.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resource.displayMetrics
    ).roundToInt()
}

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

        }

        override fun onAnimationEnd(p0: Animation?) {
            onFinishListener?.invoke()
        }

        override fun onAnimationRepeat(p0: Animation?) {
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

fun View.flyInDown(onFinishListener: (() -> Unit)? = null) {
    this.visible()
    this.alpha = 0.0f
    this.translationY = 0f
    this.translationY = -this.height.toFloat()
    // Prepare the View for the animation
    this.animate()
        .setDuration(200)
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

fun View.flyOutDown(onFinishListener: (() -> Unit)? = null) {
    this.visibility = View.VISIBLE
    this.alpha = 1.0f
    this.translationY = 0f
    // Prepare the View for the animation
    this.animate()
        .setDuration(200)
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

fun View.fadeIn(onFinishListener: (() -> Unit)? = null) {
    this.gone()
    this.alpha = 0.0f
    // Prepare the View for the animation
    this.animate()
        .setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@fadeIn.visible()
                onFinishListener?.invoke()
                super.onAnimationEnd(animation)
            }
        })
        .alpha(1.0f)
}

fun View.fadeOut(onFinishListener: (() -> Unit)? = null) {
    this.alpha = 1.0f
    // Prepare the View for the animation
    this.animate()
        .setDuration(500)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onFinishListener?.invoke()
                super.onAnimationEnd(animation)
            }
        })
        .alpha(0.0f)
}

fun View.showIn() {
    this.visible()
    this.alpha = 0f
    this.translationY = this.height.toFloat()
    this.animate()
        .setDuration(200)
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

fun View.showOut() {
    this.visible()
    this.alpha = 1f
    this.translationY = 0f
    this.animate()
        .setDuration(200)
        .translationY(this.height.toFloat())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@showOut.gone()
                super.onAnimationEnd(animation)
            }
        }).alpha(0f)
        .start()
}

fun View.rotateFab(rotate: Boolean): Boolean {
    this.animate().setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        .rotation(if (rotate) 135f else 0f)
    return rotate
}

fun View.fadeOutIn() {
    this.alpha = 0f
    val animatorSet = AnimatorSet()
    val animatorAlpha: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 0.5f, 1f)
    ObjectAnimator.ofFloat(this, "alpha", 0f).start()
    animatorAlpha.duration = (500)
    animatorSet.play(animatorAlpha)
    animatorSet.start()
}

fun View.showScale(onFinishListener: (() -> Unit)? = null) {
    this.animate()
        .scaleY(1f)
        .scaleX(1f)
        .setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onFinishListener?.invoke()
                super.onAnimationEnd(animation)
            }
        })
        .start()
}

fun View.hideScale(onFinishListener: (() -> Unit)? = null) {
    this.animate()
        .scaleY(0f)
        .scaleX(0f)
        .setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onFinishListener?.invoke()
                super.onAnimationEnd(animation)
            }
        })
        .start()
}

fun View.hideFab() {
    val moveY = 2 * this.height
    this.animate()
        .translationY(moveY.toFloat())
        .setDuration(300)
        .start()
}

fun View.showFab() {
    this.animate()
        .translationY(0f)
        .setDuration(300)
        .start()
}