package io.devchw.example.support.restdocs.enums

import io.devchw.example.support.restdocs.enums.EnumRestDocsConstants.Companion.ENUM_ADOC_PATH
import io.devchw.example.support.restdocs.enums.EnumRestDocsConstants.Companion.ENUM_DOCUMENT_ID
import io.devchw.example.support.restdocs.enums.EnumRestDocsConstants.Companion.ENUM_SNIPPET_NAME
import io.devchw.example.support.restdocs.enums.EnumRestDocsConstants.Companion.DEFAULT_SNIPPET_DIR
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream

@Component
class EnumAdocGenerator {
    fun generateEnumAdoc(enumClassNames: Set<String>) {
        // 파일이 생성될 디렉토리 삭제
        val enumAdocDirectory = File(ENUM_ADOC_PATH)
        if (enumAdocDirectory.exists()) {
            enumAdocDirectory.deleteRecursively()
        }

        for (enumClassName in enumClassNames) {
            val sb = StringBuilder()

            // 문서 상단 내용 구성
            sb.append("ifndef::snippets[]").append(System.lineSeparator())
                .append(":snippets: ").append(DEFAULT_SNIPPET_DIR).append(System.lineSeparator())
                .append("endif::[]").append(System.lineSeparator())
                .append("= ").append(enumClassName).append(System.lineSeparator())
                .append(":doctype: book").append(System.lineSeparator())
                .append(System.lineSeparator())

            // 문서 본문 내용 구성
            sb.append("include::{snippets}/$ENUM_DOCUMENT_ID/$ENUM_SNIPPET_NAME-")
                .append(enumClassName)
                .append(".adoc[]").append(System.lineSeparator())

            // Enum Asciidoc 파일들을 넣을 디렉토리가 없다면 생성
            val enumAdocDirectory = File(ENUM_ADOC_PATH)
            if (!enumAdocDirectory.exists()) {
                val isCreated = enumAdocDirectory.mkdirs()
                if (!isCreated) {
                    throw IllegalStateException("Enum asciidoc 파일을 만들 디렉토리 생성에 실패하였습니다.")
                }
            }

            val enumAdoc = File(ENUM_ADOC_PATH + "/" + enumClassName + ".adoc")

            try {
                val os = FileOutputStream(enumAdoc)
                os.write(sb.toString().toByteArray(charset("UTF-8")))
                os.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}