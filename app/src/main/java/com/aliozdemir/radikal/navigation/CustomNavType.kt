package com.aliozdemir.radikal.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.aliozdemir.radikal.domain.model.Article
import kotlinx.serialization.json.Json

object CustomNavType {

    val ArticleType = object : NavType<Article>(
        isNullableAllowed = false
    ) {

        override fun get(bundle: Bundle, key: String): Article? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Article {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Article): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Article) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}