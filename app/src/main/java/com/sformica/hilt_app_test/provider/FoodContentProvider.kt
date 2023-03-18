package com.sformica.hilt_app_test.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.sformica.hilt_app_test.di.EatDependency
import com.sformica.hilt_app_test.di.InitializerEntryPoint
import dagger.hilt.android.EarlyEntryPoints

/**
 * Food ContentProvider
 * This is sample only for Hilt test
 * @constructor Create empty Food content provider
 */
class FoodContentProvider : ContentProvider() {

    companion object {
        private val TAG: String = FoodContentProvider::class.java.simpleName
        const val CONTENT_AUTHORITY =
            "com.sformica.hilt_app_test.provider.FoodContentProvider"

        private const val CODE_PIZZA_DIR = 100
        private const val CODE_PIZZA_ITEM = 101

        const val TABLE_NAME_PIZZA = "pizza"
        const val URL_PIZZA: String =
            "content://$CONTENT_AUTHORITY/$TABLE_NAME_PIZZA"
    }

    private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private lateinit var pizzaDependency: EatDependency

    init {
        uriMatcher.apply {
            addURI(CONTENT_AUTHORITY, TABLE_NAME_PIZZA, CODE_PIZZA_ITEM)
            addURI(CONTENT_AUTHORITY, "$TABLE_NAME_PIZZA/*", CODE_PIZZA_DIR)
        }
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate()")
        context?.let {
            pizzaDependency =
                EarlyEntryPoints.get(it, InitializerEntryPoint::class.java).eatPizzaDependency()
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        Log.d(TAG, "query() uri[$uri]")
        when (val uriCode = uriMatcher.match(uri)) {
            CODE_PIZZA_ITEM,
            CODE_PIZZA_DIR -> {
                Log.d(TAG, "uriCode:$uriCode")
                pizzaDependency.eatPizzaDependency()
                return MatrixCursor(arrayOf("name", "ingredients"))
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            CODE_PIZZA_ITEM -> "vnd.android.cursor.item/$CONTENT_AUTHORITY.$TABLE_NAME_PIZZA"
            CODE_PIZZA_DIR -> "vnd.android.cursor.dir/$CONTENT_AUTHORITY.$TABLE_NAME_PIZZA"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert() uri[$uri]")
        if (context == null)
            return null

        return when (uriMatcher.match(uri)) {

            CODE_PIZZA_ITEM,
            CODE_PIZZA_DIR -> {
                values?.let {
                    context?.contentResolver?.notifyChange(uri, null)
                    ContentUris.withAppendedId(uri, 0)
                }
            }

            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "delete() uri[$uri]")
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d(TAG, "update() uri[$uri]")
        return 0
    }
}