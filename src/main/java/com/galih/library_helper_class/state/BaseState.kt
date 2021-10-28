package com.galih.library_helper_class.state

data class BaseState<out T : Any>(
    var isLoading: Boolean = false,
    val success: T? = null,
    val error: BaseStateError? = null
){
    private var loading: BaseStateLoading?=null

    fun setLoading(loading: BaseStateLoading): BaseState<T> {
        this.isLoading = true
        this.loading = loading
        return this
    }

    fun getLoadingMessage(): String? {
        return this.loading?.message
    }
}