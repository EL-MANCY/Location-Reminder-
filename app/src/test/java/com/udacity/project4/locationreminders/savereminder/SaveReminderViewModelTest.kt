package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.DummyReminderData
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDataSource: FakeDataSource

    private lateinit var viewModel: SaveReminderViewModel

    private lateinit var app: Application

    @Before
    fun setUp() {
        stopKoin()
        // initialize firebase app
        fakeDataSource = FakeDataSource()

        app = ApplicationProvider.getApplicationContext()
        // initialize viewModel with mockApp and fake repo
        viewModel = SaveReminderViewModel(app, fakeDataSource)
    }
//check if New Reminder created and values assigned to view model
    @Test
    fun saveReminder_ShowLoading() = runBlockingTest {
        // Pause dispatcher so you can verify initial values.
        mainCoroutineRule.pauseDispatcher()

        // WHEN save reminder
        viewModel.saveReminder(DummyReminderData.reminderDataItem)

        // THEN: the progress indicator is shown.
        MatcherAssert.assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))

        // Execute pending coroutines actions.
        mainCoroutineRule.resumeDispatcher()

        // THEN: the progress indicator is hidden.
        MatcherAssert.assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun saveReminder_Success() {
        // GIVEN - New Reminder created and values assigned to view model

        // WHEN save reminder
        viewModel.saveReminder(DummyReminderData.reminderDataItem)
        //THEN - assert showToast value
        MatcherAssert.assertThat(
            viewModel.showToast.getOrAwaitValue(), `is`(app.getString(R.string.reminder_saved))
        )
        Assert.assertEquals(viewModel.navigationCommand.getOrAwaitValue(), NavigationCommand.Back)
    }

    @Test
    fun validateEnteredData_TitleEmpty_ReturnFalse(){
        // GIVEN reminder with empty title
        val reminderData = DummyReminderData.reminderDataItem.copy()
        reminderData.title = ""

        // WHEN data is entered to the reminder
        val res = viewModel.validateEnteredData(reminderData)

        // THEN assert  and check the title
        MatcherAssert.assertThat(
            viewModel.showSnackBarInt.getOrAwaitValue(),
            `is`(R.string.err_enter_title)
        )
        MatcherAssert.assertThat(res, `is`(false))
    }

    @Test
    fun validateEnteredData_TitleNull_ReturnFalse(){
        // GIVEN reminder with empty title
        val reminderData = DummyReminderData.reminderDataItem.copy()
        reminderData.title = null

        // WHEN the data is validated
        val res = viewModel.validateEnteredData(reminderData)

        // THEN assert
        MatcherAssert.assertThat(
            viewModel.showSnackBarInt.getOrAwaitValue(),
            `is`(R.string.err_enter_title)
        )
        MatcherAssert.assertThat(res, `is`(false))
    }

    @Test
    fun validateEnteredData_LocationNull_ReturnFalse(){
        // GIVEN reminder with empty title
        val reminderData = DummyReminderData.reminderDataItem.copy()
        reminderData.location = null

        // WHEN the data entered
        val res = viewModel.validateEnteredData(reminderData)

        // THEN assert  and check the title
        MatcherAssert.assertThat(
            viewModel.showSnackBarInt.getOrAwaitValue(),
            Matchers.`is`(R.string.err_select_location)
        )
        //CHECK IF THE RES IS FALSE
        MatcherAssert.assertThat(res, `is`(false))
    }

    @Test
    fun validateEnteredData_LocationEmpty_ReturnFalse(){
        // GIVEN reminder with empty title
        val reminderData = DummyReminderData.reminderDataItem.copy()
        reminderData.location = ""

        // WHEN the data entered
        val res = viewModel.validateEnteredData(reminderData)

        // THEN assert  and check the title
        MatcherAssert.assertThat(
            viewModel.showSnackBarInt.getOrAwaitValue(),
            Matchers.`is`(R.string.err_select_location)
        )
        //CHECK IF THE RES IS FALSE

        MatcherAssert.assertThat(res, `is`(false))
    }

    @Test
    fun validateEnteredData_ReturnTrue() {
        // GIVEN and //WHEN
        val res = viewModel.validateEnteredData(DummyReminderData.reminderDataItem)

        // THEN CHECK THAT THE RES IS FALSE
        MatcherAssert.assertThat(res, `is`(true))
    }
}