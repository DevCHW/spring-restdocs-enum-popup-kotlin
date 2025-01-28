package io.devchw.example.support.restdocs.enums

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.hhplus.board.support.restdocs.RestDocsTestSupport
import com.hhplus.board.support.restdocs.RestDocsUtils.requestPreprocessor
import com.hhplus.board.support.restdocs.RestDocsUtils.responsePreprocessor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.payload.PayloadSubsectionExtractor
import org.springframework.restdocs.snippet.Attributes.*
import org.springframework.restdocs.snippet.Snippet

class EnumSnippetGenerator : RestDocsTestSupport() {

    private val enumAdocGenerator: EnumAdocGenerator = EnumAdocGenerator()
    private lateinit var enumQueryService: EnumQueryService
    private lateinit var enumDocumentController: EnumDocumentController

    @BeforeEach
    fun setUp() {
        enumQueryService = EnumQueryService()
        enumDocumentController = EnumDocumentController(enumQueryService)
        mockMvc = mockController(enumDocumentController)
    }

    @Test
    @DisplayName("Enum 스니펫 생성")
    fun generateEnumSnippets() {
        val enumMap = enumQueryService.getEnums()
        // asciidoc 파일 생성
        enumAdocGenerator.generateEnumAdoc(enumMap.keys)

        // when
        given()
            .get("/api/v1/enums")
            .then()
            .status(HttpStatus.OK)
            .log().all()
            .apply(
                document(
                    "enums",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    *generateEnumSnippets(enumMap)
                ),
            )
    }

    private fun generateEnumSnippets(enums: Map<String, Set<RestDocsDocumentEnum>>): Array<Snippet> {
        return enums.keys
            .map { key ->
                println("key하하 = ${key}")
                customResponseFields(
//                    beneathPath("enumMap.$key").withSubsectionId(key),
                    beneathPath(key).withSubsectionId(key),
                    attributes(key("title").value(key)),
                    *enumConvertFieldDescriptor(enums[key]!!)
                )
            }
            .toTypedArray()
    }

    private fun enumConvertFieldDescriptor(enums: Set<RestDocsDocumentEnum>): Array<FieldDescriptor> {
        return enums.map {
            fieldWithPath(it.name).description(it.description)
        }.toTypedArray<FieldDescriptor>()
    }

    private fun customResponseFields(
        subsectionExtractor: PayloadSubsectionExtractor<*>?,
        attributes: Map<String?, Any?>?,
        vararg descriptors: FieldDescriptor?
    ): CustomResponseFieldsSnippet {
        return CustomResponseFieldsSnippet(
            type = "enum-response",
            subsectionExtractor = subsectionExtractor,
            descriptors = descriptors.toList(),
            attributes = attributes,
            ignoreUndocumentedFields = true,
        )
    }
}