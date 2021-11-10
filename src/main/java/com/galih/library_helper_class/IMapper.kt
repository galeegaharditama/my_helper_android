package com.galih.library_helper_class

interface IMapper<L, R> {
    fun invoke(left: L): R
}