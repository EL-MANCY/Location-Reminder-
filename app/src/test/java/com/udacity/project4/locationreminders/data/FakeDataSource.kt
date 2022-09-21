package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.util.LinkedHashMap

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

 //its used if any exception with the database happened while fetching the data from it as maybe something unknown happened to the schema
    private var shouldReturnError = false
    //    //here in the fake data source we need to send the error by our self as its a fake data as we cant use the real schema in testig as the user dont need it so we do it by ourself
    fun setReturnError(value: Boolean) {
        //setReturnError function is used to make the fakedatasource return errors and to check it as its implemented in the ReminderListViewModelTest Class ROW NO. 83
        //we can say its like a flag or something that used to sent an error
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