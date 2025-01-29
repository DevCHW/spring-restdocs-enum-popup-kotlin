package io.devchw.example.controller.openapi3

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.hhplus.board.support.restdocs.RestDocsTestSupport
import com.hhplus.board.support.restdocs.RestDocsUtils.requestPreprocessor
import com.hhplus.board.support.restdocs.RestDocsUtils.responsePreprocessor
import io.devchw.example.controller.ExampleController
import io.devchw.example.controller.dto.request.PostExampleRequest
import io.devchw.example.enums.ExampleType
import io.devchw.example.support.restdocs.enums.generateEnumPopupLink
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.payload.JsonFieldType.*
import org.springframework.restdocs.payload.PayloadDocumentation.*

class ExampleControllerTest : RestDocsTestSupport() {

    private lateinit var exampleController: ExampleController

    @BeforeEach
    fun setup() {
        exampleController = ExampleController()
        mockMvc = mockController(exampleController)
    }

    @Test
    fun `OAS 적용 API 문서`() {
        val request = PostExampleRequest(
            name = "example",
            type = ExampleType.TYPE1,
        )

        // test
        val result = given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/api/v1/example")
            .then()
            .status(HttpStatus.OK)

        // document
        result
            .apply(
                createOpenapi3(
                    identifier = "샘플 POST API",
                    tag = "샘플",
                    summary = "샘플 API Summary",
                    description = "API 설명 Example"
                )
                    .document(
                        requestFields(
                            fieldWithPath("name").type(STRING).description("이름"),
                            fieldWithPath("type").type(STRING)
                                .description(generateEnumPopupLink("샘플 타입", ExampleType::class)),
                        ),
                        responseFields(
                            fieldWithPath("id").type(NUMBER).description("아이디"),
                            fieldWithPath("name").type(STRING).description("이름"),
                            fieldWithPath("type").type(STRING)
                                .description(generateEnumPopupLink("샘플 타입", ExampleType::class)),
                        ),
                    ),
            )
    }

    private fun createOpenapi3(
        identifier: String,
        tag: String,
        summary: String,
        description: String,
    ): RestDocumentationResultHandler {
        return document(
            identifier,
            ResourceSnippetParametersBuilder()
                .tag(tag)
                .summary(summary)
                .description(description),
            requestPreprocessor(),
            responsePreprocessor(),
        )
    }
}

