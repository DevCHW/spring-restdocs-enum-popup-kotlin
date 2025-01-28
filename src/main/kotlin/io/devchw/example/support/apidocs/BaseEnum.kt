package io.devchw.example.support.apidocs

interface BaseEnum {
    fun getName(): String {
        return this.javaClass.simpleName
    }

    fun getDescription(): String
}