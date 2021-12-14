package com.galih.library

interface IMapper<L, R> {
    fun invoke(left: L): R
}
