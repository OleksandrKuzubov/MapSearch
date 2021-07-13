package com.kutuzov.domain.model

data class Data<T>(
    var content: T? = null,
    val error: Throwable? = null,
    val loading: Boolean = false
)