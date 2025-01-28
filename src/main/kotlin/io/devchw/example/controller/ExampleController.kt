package io.devchw.example.controller

import io.devchw.example.controller.dto.request.PostExampleRequest
import io.devchw.example.controller.dto.response.GetExampleResponse
import io.devchw.example.controller.dto.response.PostExampleResponse
import io.devchw.example.enums.ExampleType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleController {

    @GetMapping("/api/v1/example")
    fun getExample(): ResponseEntity<GetExampleResponse> {
        return ResponseEntity.ok(
            GetExampleResponse(
                id = 1,
                name = "example",
                type = ExampleType.TYPE1,
            )
        )
    }

    @PostMapping("/api/v1/example")
    fun postExample(
        @RequestBody request: PostExampleRequest,
    ): ResponseEntity<PostExampleResponse> {
        return ResponseEntity.ok(
            PostExampleResponse(
                id = 1,
                name = "example",
                type = ExampleType.TYPE1,
            )
        )
    }
}