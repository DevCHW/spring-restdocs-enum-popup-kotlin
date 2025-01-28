package io.devchw.example.support.restdocs.enums

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnumDocumentController(
    private val enumQueryService: EnumQueryService,
) {
    @GetMapping("/api/v1/enums")
    fun getEnums(): ResponseEntity<Map<String, Map<String, String>>> {
        val enums = enumQueryService.getEnums()

        val responseMap = mutableMapOf<String, Map<String, String>>()
        enums.forEach { (key, value) ->
            val enumMap = mutableMapOf<String, String>()
            value.forEach {
                enumMap[it.name] = it.description
            }
            responseMap[key] = enumMap
        }
        return ResponseEntity.ok(responseMap)
    }
}