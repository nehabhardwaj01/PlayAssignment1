package models

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
                       isAdmin : Boolean = false,
                       isEnabled : Boolean = true)