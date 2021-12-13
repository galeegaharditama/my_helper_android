package com.galih.library_helper_class.validation

data class ValidationModel(
    var nameOfField: String,
    var message: String?,
    var formErrors: TypeError
) {
    constructor(nameOfField: String) : this(nameOfField, null, TypeError.VALID)

    fun setFormError(formErrors: TypeError, message: String? = null) {
        this.message = message
        this.formErrors = formErrors
        if (formErrors == TypeError.EMPTY && this.message.isNullOrEmpty()) {
            this.message = "Isian ini tidak boleh kosong."
        }
    }

    fun setFieldMessage(value: String) {
        message = value
    }
}
