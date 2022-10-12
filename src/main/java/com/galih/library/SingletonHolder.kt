package com.galih.library

/***
 * reference:
 * https://blog.mindorks.com/how-to-create-a-singleton-class-in-kotlin
 *
 * How to use:
class YourManager private constructor(context: Context) {
init {
// do something with context
}
companion object : SingletonHolder<YourManager, Context>(::YourManager)
}
 *
 */
open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {
  private var creator: ((A) -> T)? = creator

  @Volatile
  private var instance: T? = null

  fun getInstance(arg: A): T {
    val checkInstance = instance
    if (checkInstance != null) {
      return checkInstance
    }

    return synchronized(this) {
      val checkInstanceAgain = instance
      if (checkInstanceAgain != null) {
        checkInstanceAgain
      } else {
        val created = creator!!(arg)
        instance = created
        creator = null
        created
      }
    }
  }
}
