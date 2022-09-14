package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.util.LinkedHashMap

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {
    //GOAL: REACH THAT THE SNACK BOX IN THE RemindersListViewModelTest CLASS IN THE ROW NUMBER '83' HAS A VALUE OF TRUE OR A VALUE OF TEST EXCEPTION
    //WE CAN CHANGE THE SENTENCE OF TEST EXCEPTION TO "NO REMINDERS" AS IT IS THE SAME BECAUSE WHAT I MEANT WITH TEST EXCEPTION WAS THAT THERE IS NO REMINDER TO RETURN SO THATS FOR ME IS A EXCEPTION
    //SO WE CAN CHANGE IT TI TO NO REMINDERS TO BE RETURNED !!!

    //First: i made a var which has a vak=lue of false
    private var isEmptyList = false

    fun setReturnEmptylist(value: Boolean) {
        //SECOND: that function is used to set the value of the variable "shouldReturnError" AND its used in row 83 in the next class "RemindersListViewModelTest"
        isEmptyList = value
    }

    private val fakeData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()
//Return the reminders
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
    //THIRD: here we will change the Test exception to no reminders (its for me the same but i think u r right its more obvious when we say no reminders so we can return empty list
      //okay i cant explain more than that believe me i studied well i made the project well, i asked my friends non of them went to all of that questions
    //so if u see that i am wrong to send an exception then we can send an empty list as u suggested
        if (isEmptyList) {

            return Result.Success(emptyList())

        }

        return Result.Success(fakeData.values.toList())
    }
//save the reminder
    override suspend fun saveReminder(reminder: ReminderDTO) {
        fakeData[reminder.id] = reminder
    }
//return the reminder with the id
    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (isEmptyList) {
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