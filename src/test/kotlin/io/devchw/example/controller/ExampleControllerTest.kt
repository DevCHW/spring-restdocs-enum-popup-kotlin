package io.devchw.example.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.hhplus.board.support.restdocs.RestDocsTestSupport
import com.hhplus.board.support.restdocs.RestDocsUtils.requestPreprocessor
import com.hhplus.board.support.restdocs.RestDocsUtils.responsePreprocessor
import io.devchw.example.controller.dto.request.PostExampleRequest
import io.devchw.example.enums.ExampleType
import io.devchw.example.support.restdocs.enums.generateEnumPopupLink
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
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
    fun `개선 전 GET API 문서`() {
        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/example")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "before-GET",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("아이디"),
                        fieldWithPath("name").type(STRING).description("이름"),
                        fieldWithPath("type").type(STRING).description("타입(TYPE1 : 샘플 타입1 / TYPE2 : 샘플 타입2)"),
                    ),
                ),
            )
    }

    @Test
    fun `개선 전 POST API 문서`() {
        val request = PostExampleRequest(
            name = "example",
            type = ExampleType.TYPE1,
        )
        given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/api/v1/example")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "before-POST",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("name").type(STRING).description("이름"),
                        fieldWithPath("type").type(STRING).description("타입(TYPE1 : 샘플 타입1 / TYPE2 : 샘플 타입2)"),
                    ),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("아이디"),
                        fieldWithPath("name").type(STRING).description("이름"),
                        fieldWithPath("type").type(STRING).description("타입(TYPE1 : 샘플 타입1 / TYPE2 : 샘플 타입2)"),
                    ),
                ),
            )
    }

    @Test
    fun `개선 후 GET API 문서`() {
        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/example")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "after-GET",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("아이디"),
                        fieldWithPath("name").type(STRING).description("이름"),
                        fieldWithPath("type").type(STRING).description(generateEnumPopupLink("샘플 타입", ExampleType::class)),
                    ),
                ),
            )
    }

    @Test
    fun `개선 후 POST API 문서`() {
        val request = PostExampleRequest(
            name = "example",
            type = ExampleType.TYPE1,
        )
        given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/api/v1/example")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "after-POST",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("name").type(STRING).description("이름"),
                        fieldWithPath("type").type(STRING).description(generateEnumPopupLink("샘플 타입", ExampleType::class)),
                    ),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("아이디"),
                        fieldWithPath("name").type(STRING).description("이름"),
                        fieldWithPath("type").type(STRING).description(generateEnumPopupLink("샘플 타입", ExampleType::class)),
                    ),
                ),
            )
    }

}