package com.example.madminiproject

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.madminiproject.models.Feedback
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FeedbackInstrumentedTest {
    private val userId = "TestUser"
    private val database = FirebaseDatabase.getInstance().reference.child("feedbacks").child(userId)
    private val Tag = "FeedbackInstrumentedTest"

    @Before
    fun setUp() {
        // Set up the database for testing
        database.keepSynced(true)
    }

    @After
    fun tearDown() {
        // Clean up the database after testing
        database.removeValue()
    }

    @Test
    fun testCreateFeedback() {
        val feedbackId = "feedback1"
        val image = "https://feedbacktest-imgs.com/feedbackImage.jpg"
        val email = "testUser1@gmail.com"
        val rating = 0.0
        val feedback = "test feedback description"
        val author = "feedback test author"
        val userId = "1111"

        // Create the feedback and save it to the database
        val feedbackObj = Feedback(feedbackId, image, email, rating, feedback, author, userId)
        database.child(feedbackId).setValue(feedbackObj)

        // Retrieve the post from the database
        val latch = CountDownLatch(1)
        var retrievedFeedback: Feedback? = null
        database.child(feedbackId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedFeedback = task.result.getValue(Feedback::class.java)
                Log.d(Tag, "Retrieved feedback: $retrievedFeedback")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved post matches the original post
        assertNotNull(retrievedFeedback)
        assertEquals(feedbackId, retrievedFeedback?.feedbackId)
        assertEquals(image, retrievedFeedback?.image)
        assertEquals(email, retrievedFeedback?.email)
        assertEquals(rating, retrievedFeedback?.rating)
        assertEquals(feedback, retrievedFeedback?.feedback)
        assertEquals(author, retrievedFeedback?.author)
        assertEquals(userId, retrievedFeedback?.userId)
    }

    @Test
    fun testReadFeedback() {
        val feedbackId = "feedback1"
        val image = "https://feedbacktest-imgs.com/feedbackImage.jpg"
        val email = "testUser1@gmail.com"
        val rating = 0.0
        val feedback = "test feedback description"
        val author = "feedback test author"
        val userId = "1111"

        // Create the Feedback and save it to the database
        val feedbackObj = Feedback(feedbackId, image, email, rating, feedback, author, userId)
        database.child(feedbackId).setValue(feedbackObj)

        // Retrieve the post from the database
        val latch = CountDownLatch(1)
        var retrievedFeedback: Feedback? = null
        database.child(feedbackId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedFeedback = task.result.getValue(Feedback::class.java)
                Log.d(Tag, "Retrieved feedback: $retrievedFeedback")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved post matches the original post
        assertNotNull(retrievedFeedback)
        assertEquals(feedbackId, retrievedFeedback?.feedbackId)
        assertEquals(image, retrievedFeedback?.image)
        assertEquals(email, retrievedFeedback?.email)
        assertEquals(rating, retrievedFeedback?.rating)
        assertEquals(feedback, retrievedFeedback?.feedback)
        assertEquals(author, retrievedFeedback?.author)
        assertEquals(userId, retrievedFeedback?.userId)
    }

}