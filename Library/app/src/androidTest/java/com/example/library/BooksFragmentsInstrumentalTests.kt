package com.example.library

import android.app.Application
import android.content.Context

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.example.library.activities.StartActivity
import com.example.library.fragments.mainActivityFragments.*
import com.example.library.fragments.startActivityFragments.LoginFragment
import com.example.library.modules.appModule
import com.example.library.modules.dataBaseModule
import com.example.library.modules.viewModelsModule
import com.example.library.viewmodels.userViewModels.LoginViewModel
import com.github.tmurakami.dexopener.DexOpener
import org.hamcrest.Matchers
import org.junit.Assert
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
class BooksFragmentsInstrumentalTests : AndroidJUnitRunner() {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.library", appContext.packageName)
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
        loadKoinModules(listOf(appModule, dataBaseModule, viewModelsModule))
    }
    @Test
    fun profileFragmentTest() {
        val scenarioLogin = launchFragmentInContainer<ProfileFragment>()
        scenarioLogin.moveToState(Lifecycle.State.CREATED)

        Espresso.onView(ViewMatchers.withId(R.id.mybooks_profile_button))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
        Espresso.onView(ViewMatchers.withId(R.id.books_profile_button))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
        Espresso.onView(ViewMatchers.withId(R.id.create_book_button))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))

        Espresso.onView(ViewMatchers.withId(R.id.mybooks_profile_button))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.books_profile_button))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.create_book_button))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        Espresso.onView(ViewMatchers.withId(R.id.mybooks_profile_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.books_profile_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.create_book_button)).perform(ViewActions.click())

    }
    @Test
    fun createBookFragmentTest() {
        val scenarioLogin = launchFragmentInContainer<CreateBookFragment>()
        scenarioLogin.moveToState(Lifecycle.State.CREATED)

        Espresso.onView(ViewMatchers.withId(R.id.createBook_createBookView_button))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))

        Espresso.onView(ViewMatchers.withId(R.id.createBook_createBookView_button))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        Espresso.onView(ViewMatchers.withId(R.id.book_name_editText))
                .perform(ViewActions.typeText("dddd"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.createBook_createBookView_button)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.book_name_editText)).perform(ViewActions.clearText())
        Espresso.onView(ViewMatchers.withId(R.id.login_login_button)).perform(ViewActions.click())

    }
    @Test
    fun showBookFragmentTest() {
        val scenarioLogin = launchFragmentInContainer<ShowBookFragment>()
        scenarioLogin.moveToState(Lifecycle.State.CREATED)

        Espresso.onView(ViewMatchers.withId(R.id.reserveBook_Button))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))

        Espresso.onView(ViewMatchers.withId(R.id.reserveBook_Button))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        Espresso.onView(ViewMatchers.withId(R.id.reserveBook_Button)).perform(ViewActions.click())
    }
    @Test
    fun returnMyBookFragmentTest() {
        val scenarioLogin = launchFragmentInContainer<MyBooksFragment>()
        scenarioLogin.moveToState(Lifecycle.State.CREATED)

        Espresso.onView(ViewMatchers.withId(R.id.returnBook_Button))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))

        Espresso.onView(ViewMatchers.withId(R.id.returnBook_Button))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        Espresso.onView(ViewMatchers.withId(R.id.returnBook_Button)).perform(ViewActions.click())
    }
    @Test
    fun addedBookFragmentTest() {
        val scenarioLogin = launchFragmentInContainer<AddedBookFragment>()
        scenarioLogin.moveToState(Lifecycle.State.CREATED)

        Espresso.onView(ViewMatchers.withId(R.id.reserveBook_Button_addedBook))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))

        Espresso.onView(ViewMatchers.withId(R.id.return_addedBook))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))

        Espresso.onView(ViewMatchers.withId(R.id.reserveBook_Button_addedBook))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        Espresso.onView(ViewMatchers.withId(R.id.return_addedBook))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        Espresso.onView(ViewMatchers.withId(R.id.reserveBook_Button_addedBook)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.return_addedBook)).perform(ViewActions.click())
    }
}