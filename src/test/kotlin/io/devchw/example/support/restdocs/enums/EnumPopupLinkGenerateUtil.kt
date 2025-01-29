package io.devchw.example.support.restdocs.enums

import io.devchw.example.support.restdocs.enums.EnumRestDocsConstants.Companion.ENUM_DOCUMENT_ID
import kotlin.reflect.KClass

fun generateEnumPopupLink(linkText: String, documentEnumClass: KClass<*>): String {
    return "link:$ENUM_DOCUMENT_ID/${documentEnumClass.simpleName}.html[${linkText},role=\"popup\"]"
}