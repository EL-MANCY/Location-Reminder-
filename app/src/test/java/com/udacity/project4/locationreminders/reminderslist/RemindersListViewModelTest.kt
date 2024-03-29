package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.DummyReminderData
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDataSource: FakeDataSource

    private lateinit var viewModel: RemindersListViewModel
    // setup before running each test
    @Before
    fun setUp() {
        stopKoin()
        fakeDataSource = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }
    // Verify showLoading value is set to true when loading, then false after loading
    @Test
    fun loadReminders_ShowLoading() = runBlockingTest {
        // Pause dispatcher so you can verify initial values.
        mainCoroutineRule.pauseDispatcher()

        // WHEN load reminders
        viewModel.loadReminders()

        // THEN: the progress indicator is shown.
        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))

        // Execute pending coroutines actions.
        mainCoroutineRule.resumeDispatcher()

        // THEN: the progress indicator is hidden.
        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadReminders_Success() = runBlockingTest {
        // GIVEN items
        DummyReminderData.items.forEach { reminderDTO ->
            fakeDataSource.saveReminder(reminderDTO)
        }

        // WHEN load reminders
        viewModel.loadReminders()

        // THEN:
        // + data is the same with source
        val loadedItems = viewModel.remindersList.getOrAwaitValue()
        assertThat(loadedItems.size, `is`(DummyReminderData.items.size))
        for (i in loadedItems.indices) {
            assertThat(loadedItems[i].title, `is`(DummyReminderData.items[i].title))
        }
        // + showNoData is false
        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(false))
    }
    // Verify Snackbar error message value is triggered when loading reminders fails
    @Test
    fun loadReminders_DataSource_Error() = runBlockingTest {
        // GIVEN: the dataSource return errors. //FOURTH:  HERE WE USED THE FUNCTION TO GIVE IT A VALUE OF TRUE
        fakeDataSource.setReturnError(true)

        // WHEN load reminders
        viewModel.loadReminders()

        // THEN
        // + Show error message in SnackBar
        //FIFTH: WE CHECK IF THE VALUE OF showSnackBar IS "null"
        assertThat(viewModel.showSnackBar.getOrAwaitValue(), null)
        // + showNoData is true
        //SIXTH:OR ITS TRUE
        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(true))

        //SEVENTH:WE REACHED THE GOAL: IF ITS TRUE THAT MEAN THAT REMINDERS HAVE BEEN LOADED ELSE THERE IS NO REMINDERS TO BE LOADED
    }
//test if no reminder are there
    @Test
    fun loadReminders_resultSuccess_noReminders() = runBlockingTest {
        // GIVEN no items
        fakeDataSource.deleteAllReminders()

        // WHEN load reminders
        viewModel.loadReminders()

        // THEN
        // + Size is zero
        val loadedItems = viewModel.remindersList.getOrAwaitValue()
        assertThat(loadedItems.size, `is`(0))
        // + showNoData is true
        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(true))
    }
}