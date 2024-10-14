package com.example.weatherapp.utilities

import android.content.SharedPreferences
import com.example.weatherapp.utilities.SharePreferenceUtilities
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class SharePreferenceUtilitiesTest {

    private var sharePreferenceUtilities: SharePreferenceUtilities? = null

    @Mock
    var sharedPreferences: SharedPreferences? = null

    @Mock
    var editor: SharedPreferences.Editor? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initialize mocks
        sharePreferenceUtilities = SharePreferenceUtilities()

        // Mock the behavior of shared preferences and its editor
        `when`(sharedPreferences!!.edit()).thenReturn(editor)
        `when`(editor!!.putString(anyString(), anyString())).thenReturn(editor)
    }

    @Test
    fun testSetSp() {
        // Act
        sharePreferenceUtilities?.setSp("cityKey", "London", sharedPreferences)

        // Verify that the correct methods were called
        verify(editor)?.putString("cityKey", "London")
        verify(editor)?.commit()
    }

    @Test
    fun testGetSp() {
        // Arrange
        `when`(sharedPreferences?.getString("cityKey", null)).thenReturn("London")

        // Act
        val city = sharePreferenceUtilities?.getSp("cityKey", sharedPreferences)

        // Assert
        assertEquals("London", city)
        verify(sharedPreferences)?.getString("cityKey", null)
    }

}
