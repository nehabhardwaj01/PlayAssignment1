package models

import com.google.inject.Inject
import play.api.Configuration

case class Name(firstName: String,middleName: Option[String],lastName: String)

case class SignUpData( name : Name,
                       gender : String,
                       email : String,
                       mobileNo : Long,
                       username: String,
                       password: String,
                       rePassword : String,
                       age : Int,
                       hobbies : String,
                       isAdmin : Boolean )