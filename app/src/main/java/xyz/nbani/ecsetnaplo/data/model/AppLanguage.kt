package xyz.nbani.ecsetnaplo.data.model

data class AppLanguage(val tag: String, val nativeName: String)

val supportedLanguages = listOf(
    AppLanguage("en", "English"),
    AppLanguage("hu", "Magyar")
)