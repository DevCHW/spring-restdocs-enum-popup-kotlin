package io.devchw.example.support.restdocs.enums

import kotlin.reflect.KClass

const val ENUM_DOC_URL = "enums"

fun generateEnumPopupLink(linkText: String, documentEnumClass: KClass<*>): String {
    return "link:${ENUM_DOC_URL}/${documentEnumClass.simpleName}.html[${linkText},role=\"popup\"]"
}