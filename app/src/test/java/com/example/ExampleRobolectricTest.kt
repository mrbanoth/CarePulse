package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("CarePulse Doctor", appName)
  }

  @Test
  fun `home screen displays greeting upcoming appointment and navigates to treatment and video call`() {
    composeTestRule.waitForIdle()

    // Home screen content
    composeTestRule.onNodeWithText("Siyam Ahamed!").assertIsDisplayed()
    composeTestRule.onNodeWithTag("upcoming_appointment_card").assertIsDisplayed()

    // Navigate to Treatment tab
    composeTestRule.onNodeWithTag("nav_treat_tab").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("My Treatment").assertIsDisplayed()
    composeTestRule.onNodeWithText("Esther Howard").assertIsDisplayed()

    // Navigate to Meds & Clinics & Profile tabs
    composeTestRule.onNodeWithTag("nav_meds_tab").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("My Medications").assertIsDisplayed()

    composeTestRule.onNodeWithTag("nav_clinic_tab").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Partner Clinics").assertIsDisplayed()

    composeTestRule.onNodeWithTag("nav_profile_tab").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("My Profile").assertIsDisplayed()

    // Launch live video call from Profile tab
    composeTestRule.onNodeWithTag("profile_video_call_button").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("video_doctor_pill").assertIsDisplayed()
    composeTestRule.onNodeWithText("Dr. Nick Tylor").assertIsDisplayed()
    composeTestRule.onNodeWithTag("video_end_call_button").assertIsDisplayed()

    // End call returns back
    composeTestRule.onNodeWithTag("video_end_call_button").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("My Profile").assertIsDisplayed()
  }
}
