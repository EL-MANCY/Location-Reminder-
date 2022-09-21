package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.util.LinkedHashMap

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {
  //  "Why should we handle errors" , ans:After throwing an exception, an exception handler must be found to handle the exception, or the app will terminate thats why we should handle errors for any unknown error

// ITS NAME IS shouldReturnError last time it was isEmpty as it was returning an empty list and that was wrong so i was in a hurry so i named it isError
//its used in the condition which we excpect an error from the FakeDataSource as a Error Handling method
    private var shouldReturnError = false
    // i forgot to change setReturnEmpty to setReturnError as previous i was returning an empty list but its used for error handling anyways if the repo is empty or not
    fun setReturnError(value: Boolean) {
        //setReturnError function is used to make the fakedatasource return errors and to check it as its implemented in the ReminderListViewModelTest Class ROW NO. 83
        shouldReturnError = value
    }

    private val fakeData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()
//Return the reminders
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
//here we return a test exception for any error handling if there was data or not
            return Result.Error("TEST EXCEPTION")

        }
//HERE WE RETURN SUCCESS
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