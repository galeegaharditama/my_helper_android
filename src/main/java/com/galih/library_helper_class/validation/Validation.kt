package com.galih.library_helper_class.validation

import java.lang.NullPointerException

class Validation(
    private val _models: List<ValidationModel>
) {
    fun get() = _models
    fun getOnlyError() = _models.filter { it.formErrors != TypeError.VALID }

    class Builder {
        private val _models = mutableListOf<ValidationModel>()

        fun add(nameOfField: String): Builder {
            this._models.find { it.nameOfField == nameOfField }
                ?.let {
                    throw Throwable("Name Field is Exist. Try Change the Name Field")
                } ?: this._models.add(ValidationModel(nameOfField))
            return this
        }

        fun get(nameOfField: String): ValidationModel {
            return this._models.find { it.nameOfField == nameOfField }
                ?: throw NullPointerException("Name Field Doesn't Exists")
        }

        fun build(): Validation {
            require(_models.size != 0) { "Register at least one name of field" }
            return Validation(_models)
        }
    }
}