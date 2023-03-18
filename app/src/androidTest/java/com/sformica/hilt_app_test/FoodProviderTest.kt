package com.sformica.hilt_app_test

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import androidx.test.rule.provider.ProviderTestRule
import com.sformica.hilt_app_test.provider.FoodContentProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/***
 * ContentProvider test
 *
 * @property hiltRule HiltAndroidRule
 * @property mProviderRule ProviderTestRule
 * @property contentUri (Uri..Uri?)
 */
@HiltAndroidTest
class ProviderTestRunner {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mProviderRule: ProviderTestRule =
        ProviderTestRule.Builder(
            FoodContentProvider::class.java,
            FoodContentProvider.CONTENT_AUTHORITY
        ).build()

    private var contentUri = Uri.parse(FoodContentProvider.URL_PIZZA)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun verifyContentProvider() {
        val resolver: ContentResolver = mProviderRule.resolver
        val uri: Uri? = resolver.insert(contentUri, ContentValues())
        Assert.assertNotNull(uri)
    }
}