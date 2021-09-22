package com.galih.library_helper_class

interface IMapper<L, R> {
    fun map(left: L): R
    fun maps(lefts: List<L>): List<R> {
        return lefts.map {
            this.map(it)
        }
    }
}