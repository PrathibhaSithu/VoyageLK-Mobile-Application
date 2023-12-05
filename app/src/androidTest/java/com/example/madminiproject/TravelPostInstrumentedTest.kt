package com.example.madminiproject

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.madminiproject.models.Post
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TravelPostInstrumentedTest {

    private val userId = "TestUser"
    private val database = FirebaseDatabase.getInstance().reference.child("posts").child(userId)
    private val Tag = "TravelPostInstrumentedTest"

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
    fun testCreatePost() {
        val postId = "1"
        val image = "https://example.com/image.jpg"
        val title = "Test Post"
        val location = "Test Location"
        val author = "Test Author"
        val views = 0
        val content = "Test Content"

        // Create the post and save it to the database
        val post = Post(postId, image, title, location, author, views, content, userId)
        database.child(postId).setValue(post)

        // Retrieve the post from the database
        val latch = CountDownLatch(1)
        var retrievedPost: Post? = null
        database.child(postId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedPost = task.result.getValue(Post::class.java)
                Log.d(Tag, "Retrieved post: $retrievedPost")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved post matches the original post
        assertNotNull(retrievedPost)
        assertEquals(postId, retrievedPost?.postId)
        assertEquals(image, retrievedPost?.image)
        assertEquals(title, retrievedPost?.title)
        assertEquals(location, retrievedPost?.location)
        assertEquals(author, retrievedPost?.author)
        assertEquals(views, retrievedPost?.views)
        assertEquals(content, retrievedPost?.content)
        assertEquals(userId, retrievedPost?.userId)
    }

    @Test
    fun testReadPost() {
        val postId = "1"
        val image = "https://example.com/image.jpg"
        val title = "Test Post"
        val location = "Test Location"
        val author = "Test Author"
        val views = 0
        val content = "Test Content"

        // Create the post and save it to the database
        val post = Post(postId, image, title, location, author, views, content, userId)
        database.child(postId).setValue(post)

        // Retrieve the post from the database
        val latch = CountDownLatch(1)
        var retrievedPost: Post? = null
        database.child(postId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedPost = task.result.getValue(Post::class.java)
                Log.d(Tag, "Retrieved post: $retrievedPost")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved post matches the original post
        assertNotNull(retrievedPost)
        assertEquals(postId, retrievedPost?.postId)
        assertEquals(image, retrievedPost?.image)
        assertEquals(title, retrievedPost?.title)
        assertEquals(location, retrievedPost?.location)
        assertEquals(author, retrievedPost?.author)
        assertEquals(views, retrievedPost?.views)
        assertEquals(content, retrievedPost?.content)
    }

    @Test
    fun testUpdatePost() {
        val postId = "1"
        val image = "https://example.com/image.jpg"
        val title = "Test Post"
        val location = "Test Location"
        val author = "Test Author"
        val views = 0
        val content = "Test Content"

        // Create the post and save it to the database
        val post = Post(postId, image, title, location, author, views, content, userId)
        database.child(postId).setValue(post)

        // Retrieve the post from the database
        val latch = CountDownLatch(1)
        var retrievedPost: Post? = null
        database.child(postId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedPost = task.result.getValue(Post::class.java)
                Log.d(Tag, "Retrieved post: $retrievedPost")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved post matches the original post
        assertNotNull(retrievedPost)
        assertEquals(postId, retrievedPost?.postId)
        assertEquals(image, retrievedPost?.image)
        assertEquals(title, retrievedPost?.title)
        assertEquals(location, retrievedPost?.location)
        assertEquals(author, retrievedPost?.author)
        assertEquals(views, retrievedPost?.views)
        assertEquals(content, retrievedPost?.content)
        assertEquals(userId, retrievedPost?.userId)

        // Update the post and save the changes to the database
        val updatedTitle = "Updated Post"
        val updatedLocation = "Updated Location"
        val updatedContent = "Updated Content"
        retrievedPost?.title = updatedTitle
        retrievedPost?.location = updatedLocation
        retrievedPost?.content = updatedContent
        database.child(postId).setValue(retrievedPost)

        // Retrieve the updated post from the database
        val latch2 = CountDownLatch(1)
        var updatedPost: Post? = null
        database.child(postId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updatedPost = task.result.getValue(Post::class.java)
                Log.d(Tag, "Updated post: $updatedPost")
            }
            latch2.countDown()
        }
        latch2.await(10, TimeUnit.SECONDS)

        // Check that the updated post has the correct values
        assertNotNull(updatedPost)
        assertEquals(postId, updatedPost?.postId)
        assertEquals(image, updatedPost?.image)
        assertEquals(updatedTitle, updatedPost?.title)
        assertEquals(updatedLocation, updatedPost?.location)
        assertEquals(author, updatedPost?.author)
        assertEquals(views, updatedPost?.views)
        assertEquals(updatedContent, updatedPost?.content)
        assertEquals(userId, updatedPost?.userId)
    }

    @Test
    fun testDeletePost() {
        val postId = "1"
        val image = "https://example.com/image.jpg"
        val title = "Test Post"
        val location = "Test Location"
        val author = "Test Author"
        val views = 0
        val content = "Test Content"

        // Create the post and save it to the database
        val post = Post(postId, image, title, location, author, views, content, userId)
        database.child(postId).setValue(post)

        // Retrieve the post from the database
        val latch = CountDownLatch(1)
        var retrievedPost: Post? = null
        database.child(postId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedPost = task.result.getValue(Post::class.java)
                Log.d(Tag, "Retrieved post: $retrievedPost")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved post matches the original post
        assertNotNull(retrievedPost)
        assertEquals(postId, retrievedPost?.postId)
        assertEquals(image, retrievedPost?.image)
        assertEquals(title, retrievedPost?.title)
        assertEquals(location, retrievedPost?.location)
        assertEquals(author, retrievedPost?.author)
        assertEquals(views, retrievedPost?.views)
        assertEquals(content, retrievedPost?.content)

        // Delete the post from the database
        database.child(postId).removeValue()

        // Retrieve the post from the database again and make sure it's null
        val latch2 = CountDownLatch(1)
        var deletedPost: Post? = null
        database.child(postId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deletedPost = task.result.getValue(Post::class.java)
                Log.d(Tag, "Deleted post: $deletedPost")
            }
            latch2.countDown()
        }
        latch2.await(10, TimeUnit.SECONDS)

        assertNull(deletedPost)
    }

}