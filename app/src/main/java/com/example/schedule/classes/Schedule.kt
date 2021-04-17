package com.example.schedule.classes

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.Required


open class Schedule() : RealmObject() {


    @Required
    var name: String = ""

    @Required
    var description: String = ""

    @Required
    var date: String = ""


}