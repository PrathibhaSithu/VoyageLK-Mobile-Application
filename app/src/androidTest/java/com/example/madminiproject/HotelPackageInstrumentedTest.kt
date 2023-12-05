package com.example.madminiproject

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.madminiproject.models.PackageModel
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class HotelPackageInstrumentedTest {
    private val userId = "TestUser"
    private val database = FirebaseDatabase.getInstance().reference.child("packages").child(userId)
    private val Tag = "HotelPackageInstrumentedTest"

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
    fun testCreatePackage() {
        val packID = "12"
        val packImage = "https://example.com/test-pack.jpg"
        val hotelName = "Random Hotel"
        val hotelLocation = "Random Location"
        val packAuthor = "Random Author"
        val hotelPrice = 0.0
        val packDesc = "Random description. Purpose of instrumented testing"
        val contactNo = "0777899566"

        // Create the package and save it to the database
        val pack = PackageModel(packID, packImage, hotelName, hotelLocation, packAuthor, hotelPrice, packDesc, userId , contactNo)
        database.child(packID).setValue(pack)

        // Retrieve the package from the database
        val latch = CountDownLatch(1)
        var retrievedPackage: PackageModel? = null
        database.child(packID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedPackage = task.result.getValue(PackageModel::class.java)
                Log.d(Tag, "Retrieved Hotel Package: $retrievedPackage")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved package matches the original package
        assertNotNull(retrievedPackage)
        assertEquals(packID, retrievedPackage?.packID)
        assertEquals(packImage, retrievedPackage?.packImage)
        assertEquals(hotelName, retrievedPackage?.hotelName)
        assertEquals(hotelLocation, retrievedPackage?.hotelLocation)
        assertEquals(packAuthor, retrievedPackage?.packAuthor)
        assertEquals(hotelPrice, retrievedPackage?.hotelPrice)
        assertEquals(packDesc, retrievedPackage?.packDesc)
        assertEquals(contactNo, retrievedPackage?.contactNo)
        assertEquals(userId, retrievedPackage?.userId)
    }


    @Test
    fun testReadPackage() {
        val packID = "12"
        val packImage = "https://example.com/test-pack.jpg"
        val hotelName = "Random Hotel"
        val hotelLocation = "Random Location"
        val packAuthor = "Random Author"
        val hotelPrice = 0.0
        val packDesc = "Random description. Purpose of instrumented testing"
        val contactNo = "0777899566"

        // Create the package and save it to the database
        val pack = PackageModel(packID, packImage, hotelName, hotelLocation, packAuthor, hotelPrice, packDesc, userId , contactNo)
        database.child(packID).setValue(pack)

        // Retrieve the package from the database
        val latch = CountDownLatch(1)
        var retrievedPackage: PackageModel? = null
        database.child(packID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedPackage = task.result.getValue(PackageModel::class.java)
                Log.d(Tag, "Retrieved Hotel Package: $retrievedPackage")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved package matches the original package
        assertNotNull(retrievedPackage)
        assertEquals(packID, retrievedPackage?.packID)
        assertEquals(packImage, retrievedPackage?.packImage)
        assertEquals(hotelName, retrievedPackage?.hotelName)
        assertEquals(hotelLocation, retrievedPackage?.hotelLocation)
        assertEquals(packAuthor, retrievedPackage?.packAuthor)
        assertEquals(hotelPrice, retrievedPackage?.hotelPrice)
        assertEquals(packDesc, retrievedPackage?.packDesc)
        assertEquals(contactNo, retrievedPackage?.contactNo)
        assertEquals(userId, retrievedPackage?.userId)
    }

    @Test
    fun testUpdatePackage() {
        var packID = "12"
        var packImage = "https://example.com/test-pack.jpg"
        var hotelName = "Random Hotel"
        var hotelLocation = "Random Location"
        var packAuthor = "Random Author"
        var hotelPrice = 0.0
        var packDesc = "Random description. Purpose of instrumented testing"
        var contactNo = "0777899566"

        // Create the package and save it to the database
        val pack = PackageModel(packID, packImage, hotelName, hotelLocation, packAuthor, hotelPrice, packDesc, userId , contactNo)
        database.child(packID).setValue(pack)

        // Retrieve the post from the database
        val latch = CountDownLatch(1)
        var retrievedPackage: PackageModel? = null
        database.child(packID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedPackage = task.result.getValue(PackageModel::class.java)
                Log.d(Tag, "Retrieved Hotel Package: $retrievedPackage")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved package matches the original package
        assertNotNull(retrievedPackage)
        assertEquals(packID, retrievedPackage?.packID)
        assertEquals(packImage, retrievedPackage?.packImage)
        assertEquals(hotelName, retrievedPackage?.hotelName)
        assertEquals(hotelLocation, retrievedPackage?.hotelLocation)
        assertEquals(packAuthor, retrievedPackage?.packAuthor)
        assertEquals(hotelPrice, retrievedPackage?.hotelPrice)
        assertEquals(packDesc, retrievedPackage?.packDesc)
        assertEquals(contactNo, retrievedPackage?.contactNo)
        assertEquals(userId, retrievedPackage?.userId)

        // Update the package and make the changes to the database
        val updatedHotelName = "Updated Random Hotel"
        val updatedHotelLocation = "Upated Random Location"
        val updatedDescription = "Updated Random Description"
        retrievedPackage?.hotelName = updatedHotelName
        retrievedPackage?.hotelLocation = updatedHotelLocation
        retrievedPackage?.packDesc = updatedDescription
        database.child(packID).setValue(retrievedPackage)

        // Retrieve the updated post from the database
        val latch2 = CountDownLatch(1)
        var updatedPackage: PackageModel? = null
        database.child(packID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updatedPackage = task.result.getValue(PackageModel::class.java)
                Log.d(Tag, "Updated Hotel Package: $updatedPackage")
            }
            latch2.countDown()
        }
        latch2.await(10, TimeUnit.SECONDS)

        // Check that the updated post has the correct values
        assertNotNull(updatedPackage)
        assertEquals(packID, updatedPackage?.packID)
        assertEquals(packImage, updatedPackage?.packImage)
        assertEquals(updatedHotelName, updatedPackage?.hotelName)
        assertEquals(updatedHotelLocation, updatedPackage?.hotelLocation)
        assertEquals(packAuthor, updatedPackage?.packAuthor)
        assertEquals(hotelPrice, updatedPackage?.hotelPrice)
        assertEquals(updatedDescription, updatedPackage?.packDesc)
        assertEquals(contactNo, updatedPackage?.contactNo)
        assertEquals(userId, updatedPackage?.userId)
    }


    @Test
    fun testDeletePackage() {
        val packID = "12"
        val packImage = "https://example.com/test-pack.jpg"
        val hotelName = "Random Hotel"
        val hotelLocation = "Random Location"
        val packAuthor = "Random Author"
        val hotelPrice = 0.0
        val packDesc = "Random description. Purpose of instrumented testing"
        val contactNo = "0777899566"


        // Create the package and save it to the database
        val pack = PackageModel(packID, packImage, hotelName, hotelLocation, packAuthor, hotelPrice, packDesc, userId , contactNo)
        database.child(packID).setValue(pack)

        // Retrieve the post from the database
        val latch = CountDownLatch(1)
        var retrievedPackage: PackageModel? = null
        database.child(packID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                retrievedPackage = task.result.getValue(PackageModel::class.java)
                Log.d(Tag, "Retrieved Hotel Package: $retrievedPackage")
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)

        // Check that the retrieved post matches the original post
        assertNotNull(retrievedPackage)
        assertNotNull(retrievedPackage)
        assertEquals(packID, retrievedPackage?.packID)
        assertEquals(packImage, retrievedPackage?.packImage)
        assertEquals(hotelName, retrievedPackage?.hotelName)
        assertEquals(hotelLocation, retrievedPackage?.hotelLocation)
        assertEquals(packAuthor, retrievedPackage?.packAuthor)
        assertEquals(hotelPrice, retrievedPackage?.hotelPrice)
        assertEquals(packDesc, retrievedPackage?.packDesc)
        assertEquals(contactNo, retrievedPackage?.contactNo)
        assertEquals(userId, retrievedPackage?.userId)

        // Delete the package from the database
        database.child(packID).removeValue()

        // Retrieve the package from the database again and make sure it's null
        val latch2 = CountDownLatch(1)
        var deletedPackage: PackageModel? = null
        database.child(packID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deletedPackage = task.result.getValue(PackageModel::class.java)
                Log.d(Tag, "Deleted Hotel Package: $deletedPackage")
            }
            latch2.countDown()
        }
        latch2.await(10, TimeUnit.SECONDS)

        assertNull(deletedPackage)
    }


}