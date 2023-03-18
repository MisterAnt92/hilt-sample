package com.sformica.hilt_app_test.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sformica.hilt_app_test.R
import com.sformica.hilt_app_test.di.SomeComponentInitializer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SomeComponentInitializer()
    }
}
