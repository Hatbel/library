package com.example.library

import android.app.Application
import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.example.library.activities.StartActivity
import com.example.library.fragments.startActivityFragments.LoginFragment
import com.example.library.modules.appModule
import com.example.library.modules.dataBaseModule
import com.example.library.modules.viewModelsModule
import com.example.library.viewmodels.userViewModels.LoginViewModel
import com.github.tmurakami.dexopener.DexOpener
import org.hamcrest.Matchers.not
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@MediumTest
@RunWith(AndroidJUnit4::class)
class LoginRegInstrumentedTest : AndroidJUnitRunner() {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.library", appContext.packageName)
    }

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        DexOpener.install(this)
        return super.newApplication(cl, App::class.java.name, context)
    }

    @get:Rule
    var activityRule: ActivityScenarioRule<StartActivity> = ActivityScenarioRule(StartActivity::class.java)

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var loginViewModel: LoginViewModel

    private lateinit var loginFragment: LoginFragment

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule,dataBaseModule,viewModelsModule))
    }

    @Test
    fun loginFragmentTest() {
        val scenarioLogin = launchFragmentInContainer<LoginFragment>()
        scenarioLogin.moveToState(Lifecycle.State.CREATED)

        onView(withId(R.id.login_login_button))
                .check(matches(not(isEnabled())))

        onView(withId(R.id.login_login_button))
                .check(matches(isEnabled()))

        onView(withId(R.id.login_email_editText))
                .perform(typeText("dddd@dd.dd"), closeSoftKeyboard())

        onView(withId(R.id.login_password_editText))
                .perform(typeText("dddddd"), closeSoftKeyboard())

        onView(withId(R.id.login_login_button)).perform(click())
        onView(withId(R.id.register_to_register_button)).perform(click())

        onView(withId(R.id.login_email_editText)).perform(clearText())
        onView(withId(R.id.login_password_editText)).perform(clearText())
        onView(withId(R.id.login_login_button)).perform(click())
    }

    @Test
    fun registerFragmentTest() {
        val scenarioRegister = launchFragmentInContainer<LoginFragment>()
        scenarioRegister.moveToState(Lifecycle.State.CREATED)

        onView(withId(R.id.register_button))
                .check(matches(not(isEnabled())))

        onView(withId(R.id.register_button))
                .check(matches(isEnabled()))

        onView(withId(R.id.email_editText))
                .perform(typeText("dddd@dd.dd"), closeSoftKeyboard())

        onView(withId(R.id.password_editText))
                .perform(typeText("dddddd"), closeSoftKeyboard())

        onView(withId(R.id.register_button)).perform(click())

        onView(withId(R.id.email_editText)).perform(clearText())
        onView(withId(R.id.password_editText)).perform(clearText())
        onView(withId(R.id.register_button)).perform(click())
    }
}