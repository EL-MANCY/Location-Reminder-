package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.util.LinkedHashMap

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {
    /////// A fake data source to act as a double to the real data source
    private var shouldReturnError = false
    //update error flag
    //ITS USED WHEN THE LOADING REMINDERS FAILS SO IT RETURNS ITS VALUE TO FALSE TO RESET IT AFTER BEEM USED!!
    // ->  ITS USED IN "loadReminders_DataSource_Error" TEST in the REMINDERLISTVIEWMODELTEST CLASS (IT DO Verify Snackbar error message value is triggered when loading reminders fails)
    /////// A fake data source to act as a double to the real data source
    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    private val fakeData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()
//Return the reminders
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
            return Result.Error("Test exception")
        }

        return Result.Success(fakeData.values.toList())
    }
//save the reminder
    override suspend fun saveReminder(reminder: ReminderDTO) {
        fakeData[reminder.id] = reminder
    }
//return the reminder with the id
    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error("An unknown error occurred!")
        }

        val reminder = fakeData[id]
        if (reminder != null) {
            return Result.Success(reminder)
        }
        return Result.Error("Reminder not found!")
    }
//delete all the reminders
    override suspend fun deleteAllReminders() {
        fakeData.clear()
    }
}