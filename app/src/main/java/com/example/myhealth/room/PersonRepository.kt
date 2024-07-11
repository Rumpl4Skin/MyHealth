package com.example.myhealth.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myhealth.room.dao.DayDao
import com.example.myhealth.room.dao.PersonDao
import com.example.myhealth.data.Day
import com.example.myhealth.data.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*class PersonRepository (private val userDao: PersonDao, private val dayDao: DayDao){
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val person: MutableLiveData<Person> = MutableLiveData(userDao.getPerson().value)
    val days: LiveData<List<Day>> = dayDao.getDays()

    fun initDays(person: Person,days:List<Day>) {
        coroutineScope.launch(Dispatchers.IO) {
            days.forEach {
                dayDao.addDay(it)
            }

        }
    }
    fun updDays(days:List<Day>) {
        coroutineScope.launch(Dispatchers.IO) {
            days.forEach {
                dayDao.updDay(it)
            }

        }
    }

    fun addPerson(person: Person) {
        coroutineScope.launch(Dispatchers.IO) {
            userDao.addPerson(person)
        }
    }
    fun apdPerson(person: Person){
        coroutineScope.launch(Dispatchers.IO){
            userDao.addPerson(person)
        }
    }

    fun deleteUser(id:Int) {
        coroutineScope.launch(Dispatchers.IO) {
            userDao.deletePerson(id)
        }
    }
}*/