package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.util.LinkedHashMap

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {


    private var isempty = false
//SCENARIO: fake data source acts as a double to the real data source
    fun setReturnEmpty(value: Boolean) {

        isempty = value
    }

    private val fakeData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()
//Return the reminders
    override suspend fun getReminders(): Result<List<ReminderDTO>> {

        if (isempty) {

           // return Result.Success(emptyList())

// AS IT WAS DECLARED IN THE RemindersLocalRepository.KT CLASS IN A COMMENT THAT IT WOULD BE AN SUCCESS WITH THE REMINDERS OR AN ERROR WITH A """ERROR MESSAGE"""
//MY ERROR MESSAGE WAS """EXCEPTION"""
//MAYBE BECAUSE ENGLISH ISNT MY MOTHER LANGUAGE I EXPLAINED THE ERROR MESSAGE AS AN EXCEPTION THATS WHY I WROTE """EXCEPTION"""
//IT WOULD HAVE BEEN BETTER IF I SAID """NO REMINDERS FOUND""" OR SENT AN EMPTY LIST
            return Result.Error("EXCEPTION")


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
        if (isempty) {
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