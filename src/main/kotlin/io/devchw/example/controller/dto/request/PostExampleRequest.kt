package io.devchw.example.controller.dto.request

import io.devchw.example.enums.ExampleType

data class PostExampleRequest(
    val name: String,
    val type: ExampleType,
)