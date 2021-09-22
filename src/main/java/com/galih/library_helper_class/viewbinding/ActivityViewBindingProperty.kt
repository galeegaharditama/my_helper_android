package com.galih.library_helper_class.viewbinding

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * More Info at : https://vsukharew.medium.com/android-view-binding-less-boilerplate-more-delegate-69920216f063
 **/

class ActivityViewBindingProperty<T : ViewBinding>(
    private val bindingInitializer: (LayoutInflater) -> T
) : ViewBindingProperty<T>(), ReadOnlyProperty<AppCompatActivity, T> {

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        return binding ?: run {
            thisRef.lifecycle.let {
                it.addObserver(this)
                lifecycle = it
            }
            bindingInitializer.invoke(thisRef.layoutInflater).also { binding = it }
        }
    }
}