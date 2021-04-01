package com.application.onovapplication.prefs

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_FILE_NAME = "Kick Traders App"

private const val USER_ID = "user id"
private const val USER_TOKEN = "user token"
private const val ROLE = "role"
private const val PHOTO = "photo"


class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    private val mEditor: SharedPreferences.Editor = sharedPreferences.edit()

    fun clearPrefs() {
        mEditor.apply {
            clear()
            commit()
        }
    }

    fun saveUserRef(user_id: String?) {

        mEditor.putString(USER_ID, user_id)
        mEditor.apply()
    }


    fun saveRole(role: String?) {

        mEditor.putString(ROLE , role)
        mEditor.apply()
    }

    fun saveUserToken(userToken: String?) {

        mEditor.putString(USER_TOKEN, userToken)
        mEditor.apply()
    }

    fun savePhoto(photo: String?) {

        mEditor.putString(PHOTO, photo)
        mEditor.apply()
    }

    fun getRole(): String {
        return sharedPreferences.getString(ROLE, "")!!
    }

    fun getUserToken(): String {
        return sharedPreferences.getString(USER_TOKEN, "")!!

    }

    fun getUserREf(): String {
        return sharedPreferences.getString(USER_ID, "")!!
    }
    fun getUserPhoto(): String {
        return sharedPreferences.getString(PHOTO, "")!!
    }

}