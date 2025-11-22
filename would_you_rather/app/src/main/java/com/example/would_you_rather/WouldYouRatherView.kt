package com.example.would_you_rather

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

/*
    TODO: This class will be the view where users can see and select
    between two options,
    it has a post_id string, a question string,
    and two option strings.
    at first it displays the question, username of author, and
    the two choices, after the user has clicked
    one of the choices it will call Backend.choose(option, username, post_id),
    option will be an int (either 0 or 1 for left/right option),
    which will return a JSONObject that contains the updated counts
    for each option to let the user know how their answer compares.
    Probably display both the absolute counts and the percentages
*/
class WouldYouRatherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var post_id: String
    private lateinit var author: String
    private lateinit var option1: String
    private lateinit var option2: String
    private lateinit var option1Count: Integer
    private lateinit var option2Count: Integer
    private lateinit var currentUser: String

    private val authorText: TextView
    private val optionAText: TextView
    private val optionBText: TextView

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_would_you_rather, this, true)

        authorText = findViewById(R.id.authorText)
        optionAText = findViewById(R.id.optionA)
        optionBText = findViewById(R.id.optionB)
    }

    fun setPost(post: Post) {
        this.post_id = post.post_id
        this.author = post.author
        this.option1 = post.option1
        this.option2 = post.option2
        this.option1Count = post.option1Count
        this.option2Count = post.option2Count

        authorText.text = "Posted by ${post.author}"
        optionAText.text = post.option1
        optionBText.text = post.option2

        optionAText.setOnClickListener { handleChooseOption(0) }
        optionBText.setOnClickListener { handleChooseOption(1) }
    }

    private fun handleChooseOption(optionIndex: Int) {
        Backend.choose(optionIndex, post_id, currentUser)

        val total = option1Count.toInt() + option2Count.toInt()
        val pctA = if (total > 0) (100 * option1Count.toInt() / total) else 0
        val pctB = if (total > 0) (100 * option2Count.toInt() / total) else 0

        optionAText.text = "$option1Count votes ($pctA%)\n"
        optionBText.text = "$option2Count votes ($pctB%)\n"
    }
}