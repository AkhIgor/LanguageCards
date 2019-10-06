package com.igor.langugecards.model

data class TranslateSettings(var languageFrom : String?,
                             val languageCodeFrom : String?,
                             var languageTo : String?,
                             var languageCodeTo : String?) {
    val autoDetectingLanguage : String = "Автоматически"
}
