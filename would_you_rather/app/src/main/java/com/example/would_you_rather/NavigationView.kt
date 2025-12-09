package com.example.would_you_rather

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout

class NavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val homeButton: Button
    private val postButton: Button

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.navigation_view, this, true)
        homeButton = findViewById(R.id.nav_home)
        postButton = findViewById(R.id.nav_post)
    }

    fun setOnHomeClick(listener: OnClickListener?) {
        homeButton.setOnClickListener(listener)
    }

    fun setOnPostClick(listener: OnClickListener?) {
        postButton.setOnClickListener(listener)
    }
}
