package com.igor.langugecards.model

import com.igor.langugecards.constants.Constants

data class TranslateSettings(var languageFrom : String,
                             val languageCodeFrom : String = Constants.EMPTY_STRING,
                             var languageTo : String = Constants.EMPTY_STRING,
                             var languageCodeTo : String = Constants.EMPTY_STRING)
