package com.example.myapplication.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class GenericJsonDeserializer<T> : JsonDeserializer<T> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): T {
        if (json.isJsonObject) {
            // Handle object case
            return context.deserialize(json, typeOfT)
        } else if (json.isJsonArray) {
            // Handle array case
            return context.deserialize(json, typeOfT)
        } else {
            throw JsonParseException("Unexpected JSON structure: Neither object nor array.")
        }
    }
}
