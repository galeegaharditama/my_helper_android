package com.galih.library.viewbinding

import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * More Info at : https://vsukharew.medium.com/android-view-binding-less-boilerplate-more-delegate-69920216f063
 **/

/**
 * wrapper above [FragmentViewBindingProperty]
 */
fun <T : ViewBinding> fragmentViewBinding(
    viewBinder: (View) -> T
): FragmentViewBindingProperty<T> = FragmentViewBindingProperty(viewBinder)

/**
 * wrapper above [ActivityViewBindingProperty]
 */
fun <T : ViewBinding> activityViewBinding(
    bindingInitializer: (LayoutInflater) -> T
): ActivityViewBindingProperty<T> = ActivityViewBindingProperty(bindingInitializer)
