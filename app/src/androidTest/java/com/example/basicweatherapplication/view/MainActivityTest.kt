package com.example.basicweatherapplication.view

import android.os.Build
import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import com.example.basicweatherapplication.R
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun `verify_that_weather_details_are_not_populated_when_location_permissions_are_not_granted`() {
        var permissionPosition = 2
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            permissionPosition = 1
        provideLocationPermissions(permissionPosition = permissionPosition)
        onView(withText("Welcome to the Weather App")).check(matches(isDisplayed()))
        onView(withText("Get Data")).check(matches(isDisplayed()))
        onView(withId(R.id.inputText)).check(matches(withText("")))
        onView(withId(R.id.temperatureText)).check(matches(withText("")))
        onView(withId(R.id.descriptionText)).check(matches(withText("")))
    }

    @Test
    fun `verify_that_weather_details_are_populated_when_location_permissions_are_granted`() {
        var permissionPosition = 1
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            permissionPosition = 0
        provideLocationPermissions(permissionPosition = permissionPosition)
        onView(withText("Welcome to the Weather App")).check(matches(isDisplayed()))
        onView(withText("Get Data")).check(matches(isDisplayed()))
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.inputText)).check(matches(not(withText(""))))
        onView(withId(R.id.temperatureText)).check(matches(not(withText(""))))
        onView(withId(R.id.descriptionText)).check(matches(not(withText(""))))
    }

    @Test
    fun `verify_that_weather_details_are_populated_when_default_city_is_populated_and_get_Data_is_clicked`() {
        var permissionPosition = 1
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            permissionPosition = 0
        provideLocationPermissions(permissionPosition = permissionPosition)
        onView(withText("Welcome to the Weather App")).check(matches(isDisplayed()))
        onView(withText("Get Data")).check(matches(isDisplayed()))
        Thread.sleep(ONE_SECOND)
        onView(withText("Get Data")).perform(click())
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.inputText)).check(matches(not(withText(""))))
        onView(withId(R.id.temperatureText)).check(matches(not(withText(""))))
        onView(withId(R.id.descriptionText)).check(matches(not(withText(""))))
    }

    @Test
    fun `verify_that_weather_details_are_populated_when_valid_city_is_entered_and_get_Data_is_clicked`() {
        var permissionPosition = 1
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            permissionPosition = 0
        provideLocationPermissions(permissionPosition = permissionPosition)
        onView(withText("Welcome to the Weather App")).check(matches(isDisplayed()))
        onView(withText("Get Data")).check(matches(isDisplayed()))
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.inputText))
            .check(matches(not(withText(""))))
        onView(withId(R.id.inputText))
            .perform(clearText())
            .perform(typeText("New Delhi"))
        onView(withText("Get Data")).perform(click())
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.temperatureText))
            .check(matches(not(withText(""))))
        onView(withId(R.id.descriptionText))
            .check(matches(not(withText(""))))
    }

    @Test
    fun `verify_that_weather_details_are_not_populated_when_invalid_city_is_entered_and_get_Data_is_clicked_and_app_is_Relaunched`() {
        var permissionPosition = 1
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            permissionPosition = 0
        provideLocationPermissions(permissionPosition = permissionPosition)
        onView(withText("Welcome to the Weather App")).check(matches(isDisplayed()))
        onView(withText("Get Data")).check(matches(isDisplayed()))
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.inputText))
            .check(matches(not(withText(""))))
        onView(withId(R.id.inputText))
            .perform(clearText())
            .perform(typeText("New Del"))
        onView(withText("Get Data")).perform(click())
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.errorText))
            .check(matches(withText("Please enter valid city")))
        onView(withId(R.id.temperatureText))
            .check(matches(withText("")))
        onView(withId(R.id.descriptionText))
            .check(matches(withText("")))
        activityScenarioRule.scenario.recreate()
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.errorText))
            .check(matches(withText("Please enter valid city")))
    }

    @Test
    fun `verify_that_weather_details_are_not_populated_when_valid_city_is_entered_and_get_Data_is_clicked_and_app_is_Relaunched`() {
        var permissionPosition = 1
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            permissionPosition = 0
        provideLocationPermissions(permissionPosition = permissionPosition)
        onView(withText("Welcome to the Weather App")).check(matches(isDisplayed()))
        onView(withText("Get Data")).check(matches(isDisplayed()))
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.inputText))
            .check(matches(not(withText(""))))
        onView(withId(R.id.inputText))
            .perform(clearText())
            .perform(typeText("New Delhi"))
        onView(withText("Get Data")).perform(click())
        Thread.sleep(ONE_SECOND)
        onView(withId(R.id.temperatureText))
            .check(matches(not(withText(""))))
        onView(withId(R.id.descriptionText))
            .check(matches(not(withText(""))))
        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.inputText))
            .check(matches(withText("New Delhi")))
    }

    private fun provideLocationPermissions(permissionPosition: Int = 0) {
        try {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                val allowPermissions: UiObject = device.findObject(UiSelector().clickable(true).checkable(false).index(permissionPosition))
                if (allowPermissions.exists()) {
                    try {
                        allowPermissions.click()
                    } catch (e: UiObjectNotFoundException) {
                        Log.e("all_the_Views_are_Visible", "There is no permissions dialog to interact with")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("all_the_Views_are_Visible", "Error handling permissions dialog", e)
        }
    }

    companion object {
        private const val HALF_SECOND = 500L
        private const val ONE_SECOND = 1000L
        private const val TWO_SECONDS = 2000L
    }
}