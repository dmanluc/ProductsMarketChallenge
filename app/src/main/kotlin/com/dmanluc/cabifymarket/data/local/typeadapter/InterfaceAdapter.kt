package com.dmanluc.cabifymarket.data.local.typeadapter

import com.google.gson.*
import java.lang.reflect.Type


/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-09-01.
 */
class InterfaceAdapter<T : Any> : JsonDeserializer<T>, JsonSerializer<T> {

    companion object {
        private const val CLASSNAME = "CLASSNAME"
        private const val DATA = "DATA"
    }

    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement,
                             type: Type,
                             jsonDeserializationContext: JsonDeserializationContext): T {
        val jsonObject = jsonElement.asJsonObject
        val prim = jsonObject.get(CLASSNAME) as JsonPrimitive
        val className = prim.asString
        val klass = getObjectClass(className)
        return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass)
    }


    override fun serialize(jsonElement: T,
                           type: Type,
                           jsonSerializationContext: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            addProperty(CLASSNAME, jsonElement::class.java.name)
            add(DATA, jsonSerializationContext.serialize(jsonElement))
        }
    }

    private fun getObjectClass(className: String): Class<*> {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw JsonParseException(e.message)
        }
    }

}