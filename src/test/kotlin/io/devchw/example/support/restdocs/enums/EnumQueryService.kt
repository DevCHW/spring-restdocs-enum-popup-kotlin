package io.devchw.example.support.restdocs.enums

import org.springframework.stereotype.Component
import java.io.File

@Component
class EnumQueryService {

    fun getEnums(): Map<String, Set<RestDocsDocumentEnum>> {
        val enumClasses = getEnumClassesFromPackage("io.devchw.example")

        return enumClasses
            .associateBy(
                keySelector = { it.simpleName },
                valueTransform = {
                    getEnumConstants2(it)
                }
            )
    }

    private fun getEnumConstants2(enumClass: Class<*>): Set<RestDocsDocumentEnum> {
        // enum 클래스의 모든 enum 상수를 가져옵니다.
        val enumConstants = enumClass.enumConstants

        // 결과를 저장할 Set 생성
        val restDocsDocumentEnumSet = mutableSetOf<RestDocsDocumentEnum>()

        // enum 상수들을 순회하면서 각 상수에 대해 리플렉션을 사용해 description을 가져옵니다.
        enumConstants.forEach { enumConstant ->
            try {
                val descriptionField = enumClass.getDeclaredField("description")
                descriptionField.isAccessible = true // private 필드 접근 허용

                val description = descriptionField.get(enumConstant) as String

                val restDocsDocumentEnum = RestDocsDocumentEnum(
                    name = enumConstant.toString(),
                    description = description
                )
                println("Enum name: ${enumConstant}, Description: $description")

                restDocsDocumentEnumSet.add(restDocsDocumentEnum)
            } catch (e: NoSuchFieldException) {
                // description 필드가 없을 경우 처리
                println("No description field found for enum: ${enumConstant}")
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
        return restDocsDocumentEnumSet
    }

    // 프로젝트에서 Enum 클래스 불러오기
    fun getEnumClassesFromPackage(packageName: String): Set<Class<*>> {
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
                        // 예외가 발생하면 넘어감
                    }
                }
            }
        }
        return enumClasses
    }
}