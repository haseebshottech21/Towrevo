package com.Towrevo.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName



@Entity(tableName = "user_table")
data class User (
	@SerializedName("id")
	@PrimaryKey
	@ColumnInfo(name = "id")
	val id : Int,

	@SerializedName("email")
	@ColumnInfo(name= "email")
	val email : String,

	@SerializedName("first_name")
	@ColumnInfo(name="first_name")
	val firstName : String,

	@SerializedName(value="last_name")
	@ColumnInfo(name="last_name")
	val lastName : String,

	@SerializedName(value="avatar")
	@ColumnInfo(name="avatar")
	val avatar : String

)