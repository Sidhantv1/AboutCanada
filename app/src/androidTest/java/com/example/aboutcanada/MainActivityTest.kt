package com.example.aboutcanada

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.aboutcanada.ui.MainActivity
import junit.framework.TestCase
import org.junit.Test

/**
 * MainActivity Test Cases
 */
class MainActivityTest : TestCase() {

    /**
     * Test case for the main activity in view
     */
    @Test
    fun test_isActivityInView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.clMain)).check(matches(isDisplayed()))
    }

    /**
     * Test case for gone visibility of no internet connection available text message
     */
    @Test
    fun test_visibility_title() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.tvNoInternetMsg)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }


    /**
     * Test case for visibility of progress bar
     */
    @Test
    fun test_visibility_progressbar() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * Test case for matching the no internet message text
     */
    @Test
    fun test_isNoInternetMessageDisplayed() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.tvNoInternetMsg)).check(matches(withText(R.string.no_internet_message)))
    }

    /**
     * Test case for Recycler view comes into view
     */
    @Test
    fun test_isListVisible_onAppLaunch() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    /**
     * Test case for Recycler view scrolling to specific index
     */
    @Test
    fun test_recyclerViewTest() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.recyclerView)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(2)
        )
    }

    /**
     * Test case for Recycler view action on specific index
     */
    @Test
    fun test_recyclerView_perform_click() {
        Handler(Looper.getMainLooper()).postDelayed({
            val activityScenario = ActivityScenario.launch(MainActivity::class.java)
            onView(withId(R.id.recyclerView)).perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )
        }, 2000)
    }

}