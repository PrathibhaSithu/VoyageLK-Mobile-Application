package com.example.madminiproject

import com.example.madminiproject.models.Feedback
import org.junit.Assert.assertEquals
import org.junit.Test

class FeedbackUnitTest {

    @Test
    fun testCreateFeedback() {
        val feedbackId = "feedback1"
        val image = "https://feedbacktest-imgs.com/feedbackImage.jpg"
        val email = "testUser1@gmail.com"
        val rating = 0.0
        val feedback = "test feedback description"
        val author = "feedback test author"
        val userId = "1111"

        // Create a feedback object with the input values
        val feedbackObj = Feedback(feedbackId, image, email, rating, feedback, author, userId)

        // Verify that each property of the feedback object matches the input value
        assertEquals(feedbackId, feedbackObj.feedbackId)
        assertEquals(image, feedbackObj.image)
        assertEquals(email, feedbackObj.email)
        assertEquals(rating, feedbackObj.rating,0.0)
        assertEquals(feedback, feedbackObj.feedback)
        assertEquals(author, feedbackObj.author)
        assertEquals(userId, feedbackObj.userId)
    }

    @Test
    fun testEquals() {
        val feedbackId = "feedback1"
        val image = "https://feedbacktest-imgs.com/feedbackImage.jpg"
        val email = "testUser1@gmail.com"
        val rating = 0.0
        val feedback = "test feedback description"
        val author = "feedback test author"
        val userId = "1111"

        val feedback1 = Feedback(feedbackId, image, email, rating, feedback, author, userId)
        val feedback2 = Feedback(feedbackId, image, email, rating, feedback, author, userId)

        assertEquals(feedback1, feedback2)
    }
}