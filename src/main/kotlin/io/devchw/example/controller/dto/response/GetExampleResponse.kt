package io.devchw.example.controller.dto.response

import io.devchw.example.enums.ExampleType

data class GetExampleResponse(
    val id: Long,
    val name: String,
    val type: ExampleType,
)