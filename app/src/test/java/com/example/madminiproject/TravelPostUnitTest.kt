package com.example.madminiproject

import com.example.madminiproject.models.Post
import org.junit.Assert.assertEquals
import org.junit.Test

class PostUnitTest {
    @Test
    fun testCreatePost() {
        val postId = "1"
        val image = "https://example.com/image.jpg"
        val title = "Test Post"
        val location = "Test Location"
        val author = "Test Author"
        val views = 0
        val content = "Test Content"
        val userId = "1234"

        val post = Post(postId, image, title, location, author, views, content, userId)

        assertEquals(postId, post.postId)
        assertEquals(image, post.image)
        assertEquals(title, post.title)
        assertEquals(location, post.location)
        assertEquals(author, post.author)
        assertEquals(views, post.views)
        assertEquals(content, post.content)
        assertEquals(userId, post.userId)
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
        val userId = "1234"

        val post = Post(postId, image, title, location, author, views, content, userId)

        val updatedImage = "https://example.com/updated_image.jpg"
        val updatedTitle = "Updated Test Post"
        val updatedLocation = "Updated Test Location"
        val updatedAuthor = "Updated Test Author"
        val updatedViews = 1
        val updatedContent = "Updated Test Content"

        post.image = updatedImage
        post.title = updatedTitle
        post.location = updatedLocation
        post.author = updatedAuthor
        post.views = updatedViews
        post.content = updatedContent

        assertEquals(updatedImage, post.image)
        assertEquals(updatedTitle, post.title)
        assertEquals(updatedLocation, post.location)
        assertEquals(updatedAuthor, post.author)
        assertEquals(updatedViews, post.views)
        assertEquals(updatedContent, post.content)
    }

    @Test
    fun testEquals() {
        val postId = "1"
        val image = "https://example.com/image.jpg"
        val title = "Test Post"
        val location = "Test Location"
        val author = "Test Author"
        val views = 0
        val content = "Test Content"
        val userId = "1234"

        val post1 = Post(postId, image, title, location, author, views, content, userId)
        val post2 = Post(postId, image, title, location, author, views, content, userId)

        assertEquals(post1, post2)
    }
}