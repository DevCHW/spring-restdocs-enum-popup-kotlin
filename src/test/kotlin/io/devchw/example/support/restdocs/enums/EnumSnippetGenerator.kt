package io.devchw.example.support.restdocs.enums

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
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
    private lateinit var enumMetadataReader: EnumMetadataReader
    private lateinit var enumDocumentController: EnumDocumentController

    @BeforeEach
    fun setUp() {
        enumMetadataReader = EnumMetadataReader()
        enumDocumentController = EnumDocumentController(enumMetadataReader)
        mockMvc = mockController(enumDocumentController)
    }

    @Test
    @DisplayName("Enum 스니펫 생성")
    fun generateEnumSnippets() {
        val enumMap = enumMetadataReader.getEnumsMetadataMap()
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
                    ResourceSnippetParametersBuilder()
                        .privateResource(true),
                    requestPreprocessor(),
                    responsePreprocessor(),
                ).document(
                    *generateEnumSnippets(enumMap),
                ),
            )
    }

    private fun generateEnumSnippets(enums: Map<String, Set<EnumMetadata>>): Array<Snippet> {
        return enums.keys
            .map { key ->
                customResponseFields(
                    beneathPath(key).withSubsectionId(key),
                    attributes(key("title").value(key)),
                    *enumConvertFieldDescriptor(enums[key]!!)
                )
            }
            .toTypedArray()
    }

    private fun enumConvertFieldDescriptor(enums: Set<EnumMetadata>): Array<FieldDescriptor> {
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