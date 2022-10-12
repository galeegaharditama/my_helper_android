package com.galeegaharditama.helper.validation

class Validation(
  private val models: List<ValidationModel>
) {
  fun get() = models
  fun getOnlyError() = models.filter { it.formErrors != ErrorValidationType.VALID }

  class Builder {
    private val _models = mutableListOf<ValidationModel>()

    fun add(nameOfField: String): Builder {
      this._models.find { it.nameOfField == nameOfField }
        ?.let { throw NameFieldDuplicateException() } ?: this._models.add(
        ValidationModel(
          nameOfField
        )
      )
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

class NameFieldDuplicateException(
  errorMessage: String = "Name Field is Exist. Try Change the Name Field"
) : Throwable(errorMessage)
