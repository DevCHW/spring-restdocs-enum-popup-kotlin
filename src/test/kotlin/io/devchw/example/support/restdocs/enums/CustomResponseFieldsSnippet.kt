package io.devchw.example.support.restdocs.enums

import org.springframework.http.MediaType
import org.springframework.restdocs.operation.Operation
import org.springframework.restdocs.payload.AbstractFieldsSnippet
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadSubsectionExtractor

class CustomResponseFieldsSnippet(
    type: String?,
    subsectionExtractor: PayloadSubsectionExtractor<*>?,
    descriptors: List<FieldDescriptor?>?,
    attributes: Map<String?, Any?>?,
    ignoreUndocumentedFields: Boolean,
) : AbstractFieldsSnippet(
    type, descriptors, attributes, ignoreUndocumentedFields, subsectionExtractor
) {
    override fun getContentType(operation: Operation): MediaType {
        return operation.response.headers.contentType!!
    }

    override fun getContent(operation: Operation): ByteArray {
        return operation.response.content
    }
}