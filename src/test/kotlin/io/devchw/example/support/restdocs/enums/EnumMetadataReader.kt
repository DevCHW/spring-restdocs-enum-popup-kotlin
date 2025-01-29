package io.devchw.example.support.restdocs.enums

import io.devchw.example.support.restdocs.enums.EnumRestDocsConstants.Companion.ENUM_BASE_PACKAGE
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class EnumMetadataReader {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun getEnumsMetadataMap(): Map<String, Set<EnumMetadata>> {
        val enumClasses = getEnumClassesFromPackage(ENUM_BASE_PACKAGE)

        return enumClasses
            .associateBy(
                keySelector = { it.simpleName },
                valueTransform = {
                    getEnumConstants(it)
                }
            )
    }

    // 프로젝트에서 packageName 하위에 위치한 Enum 클래스 불러오기
    private fun getEnumClassesFromPackage(packageName: String): Set<Class<*>> {
        val path = packageName.replace('.', '/')
        val resources = Thread.currentThread().contextClassLoader.getResources(path)

        val enumClasses = mutableSetOf<Class<*>>()
        while (resources.hasMoreElements()) {
            val resource = resources.nextElement()
            val file = File(resource.toURI())

            // 파일에서 모든 .class 파일을 검색
            file.walkTopDown().forEach { classFile ->
                if (classFile.name.endsWith(".class")) {
                    // 클래스 이름을 가져와서 Class로 변환
                    val className = "${packageName}.${classFile.relativeTo(file).path.replace(File.separatorChar, '.')}"
                        .removeSuffix(".class")
                    try {
                        val clazz = Class.forName(className)
                        if (clazz.isEnum) {
                            enumClasses.add(clazz)
                        }
                    } catch (e: Exception) {
                        log.error(e.message, e)
                    }
                }
            }
        }
        return enumClasses
    }

    // Enum클래스의 상수 값들을 Set<EnumMetadata>로 가공
    private fun getEnumConstants(enumClass: Class<*>): Set<EnumMetadata> {
        // enum 클래스의 모든 상수를 꺼낸다
        val enumConstants = enumClass.enumConstants

        // 결과를 저장할 Set 생성
        val enumMetadataSet = mutableSetOf<EnumMetadata>()

        // enum 상수들을 순회하면서 각 상수에 대해 리플렉션을 사용해 description 필드 읽기.
        enumConstants.forEach { enumConstant ->
            try {
                val descriptionField = enumClass.getDeclaredField("description")
                descriptionField.isAccessible = true // private 필드 접근 허용

                val description = descriptionField.get(enumConstant) as String

                val restDocsDocumentEnum = EnumMetadata(
                    name = enumConstant.toString(),
                    description = description
                )
                enumMetadataSet.add(restDocsDocumentEnum)
            } catch (e: NoSuchFieldException) {
                log.error("${enumClass.simpleName} 클래스에 description 필드가 존재하지 않습니다.")
            } catch (e: Exception) {
                log.error(e.message, e)
            }
        }
        return enumMetadataSet
    }
}