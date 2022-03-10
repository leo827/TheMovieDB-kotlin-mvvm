package com.yuzu.themoviedb.view.activity

import android.os.Bundle
import com.yuzu.themoviedb.R
import com.yuzu.themoviedb.view.fragment.MovieFragment

class MainActivity: BaseViewActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_content,
                    MovieFragment()
                ).commit()
        }
    }
}