package com.galeegaharditama.helper.validation

data class ValidationModel(
  var nameOfField: String,
  var message: String?,
  var formErrors: ErrorValidationType
) {
  constructor(nameOfField: String) : this(nameOfField, null, ErrorValidationType.VALID)

  fun setFormError(formErrors: ErrorValidationType, message: String? = null) {
    this.message = message
    this.formErrors = formErrors
    if (formErrors == ErrorValidationType.EMPTY && this.message.isNullOrEmpty()) {
      this.message = "Isian ini tidak boleh kosong."
    }
  }

  fun setFieldMessage(value: String) {
    message = value
  }
}
