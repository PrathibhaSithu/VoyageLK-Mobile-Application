package com.example.madminiproject

import com.example.madminiproject.models.PackageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class HotelPackageUnitTest {

    @Test
    fun testCreatePackage() {
        val packID = "test-pack1"
        val packImage = "https://testing-case-imgs/pack-jpg"
        val hotelName = "testing random hotel"
        val hotelLocation = "testing random location"
        val packAuthor = "test pack author"
        val hotelPrice = 0.0
        val packDesc = "description random for testing purpose"
        val userId = "hotel-owner-101"
        val contactNo = "0714947562"

        val pack = PackageModel(packID, packImage, hotelName, hotelLocation, packAuthor, hotelPrice, packDesc, userId , contactNo)

        assertEquals(packID, pack.packID)
        assertEquals(packImage, pack.packImage)
        assertEquals(hotelName, pack.hotelName)
        assertEquals(hotelLocation, pack.hotelLocation)
        assertEquals(packAuthor, pack.packAuthor)
        assertEquals(hotelPrice, pack.hotelPrice,0.0)
        assertEquals(packDesc, pack.packDesc)
        assertEquals(userId, pack.userId)
        assertEquals(contactNo, pack.contactNo)
    }


    @Test
    fun testUpdatePackage() {
        val packID = "test-pack1"
        val packImage = "https://testing-case-imgs/pack-jpg"
        val hotelName = "testing random hotel"
        val hotelLocation = "testing random location"
        val packAuthor = "test pack author"
        val hotelPrice = 0.0
        val packDesc = "Random description for testing purpose"
        val userId = "hotel-owner-101"
        val contactNo = "0714947562"

        val pack = PackageModel(packID, packImage, hotelName, hotelLocation, packAuthor, hotelPrice, packDesc, userId, contactNo)

        val updatedPackImage = "https://testing-case-imgs/updated-pack-jpg"
        val updatedHotelName = "Updated Random Hotel Name"
        val updatedHotelLocation = "Updated Random Hotel Location"
        val updatedPackAuthor = "Updated Random Package Author"
        val updatedHotelPrice = 12750.0
        val updatedPackDescription = "Updated pack description"

        pack.packImage = updatedPackImage
        pack.hotelName = updatedHotelName
        pack.hotelLocation = updatedHotelLocation
        pack.packAuthor = updatedPackAuthor
        pack.hotelPrice = updatedHotelPrice
        pack.packDesc = updatedPackDescription

        assertEquals(updatedPackImage, pack.packImage)
        assertEquals(updatedHotelName, pack.hotelName)
        assertEquals(updatedHotelLocation, pack.hotelLocation)
        assertEquals(updatedPackAuthor, pack.packAuthor)
        assertEquals(updatedHotelPrice, pack.hotelPrice, 0.0)
        assertEquals(updatedPackDescription, pack.packDesc)
        assertEquals(contactNo,pack.contactNo)
    }

    @Test
    fun testEquals() {
        val packID = "test-pack1"
        val packImage = "https://testing-case-imgs/pack-jpg"
        val hotelName = "testing random hotel"
        val hotelLocation = "testing random location"
        val packAuthor = "test pack author"
        val hotelPrice = 0.0
        val packDesc = "Random description for testing purpose"
        val userId = "hotel-owner-101"
        val contactNo = "0714947562"

        val testPack1 = PackageModel(packID, packImage, hotelName, hotelLocation, packAuthor, hotelPrice, packDesc, userId, contactNo)
        val testPack2 = PackageModel(packID, packImage, hotelName, hotelLocation, packAuthor, hotelPrice, packDesc, userId , contactNo)

        assertEquals(testPack1, testPack2)
    }


}