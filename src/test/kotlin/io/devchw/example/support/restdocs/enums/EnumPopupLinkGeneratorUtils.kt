package io.devchw.example.support.restdocs.enums

import kotlin.reflect.KClass

fun generateEnumPopupLink(description: String, documentEnumClass: KClass<*>): String {
    return String.format(
        "link:enums/%s.html[%s,role=\"popup\"]",
        documentEnumClass.simpleName,
        description
    )
}