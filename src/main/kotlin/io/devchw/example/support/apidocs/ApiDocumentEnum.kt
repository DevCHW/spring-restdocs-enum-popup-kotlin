package io.devchw.example.support.apidocs

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiDocumentEnum(
    val value: KClass<out BaseEnum> = BaseEnum::class,
    val description: String,
    val methodName: String = "of"
)