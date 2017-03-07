package controllers

import com.google.inject.Singleton
import play.api.data._
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.data.Forms._
import models.{Name, SignUpData, SignInData}
import services.Operations

@Singleton
class SignInController extends Controller{
  val name = new Name(" ",Some(" ")," ")
  val signUpData = SignUpData(name,"","",0,"","","",0,"")
  val signInData = SignInData("","")

  val userForm = Form(                                //Form is to transform form data into a bound instance of a case class
    mapping(                                          //mapping takes 3 parameters
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(SignInData.apply)(SignInData.unapply)
  )

  def userPost = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        println(formWithErrors)
        BadRequest(views.html.signIn(signInData)(" "+formWithErrors))
      },
      userData => {
        println(userData)
        println(Operations.getUsers)
        val (exist,message,obj) = validate(userData)
        println(exist)
        println(message)
        println(obj)

        if(exist){
          Ok(views.html.profile(obj))
        }
        else{
          Ok(views.html.signIn(userData)(message))
        }
      }
    )
  }

  def validate(user : SignInData) = {
    val usernameList= Operations.getUsers.map(_.username)
    if(usernameList.contains(user.username)){
      val u = Operations.getUser(user.username)
      if(u.password == user.password){
        (true,"Sign In Successful",u)
      }
      else{
        (false,"Incorrect Password",signUpData)
      }
    }
    else{
      (false,"Username doesnt exists",signUpData)
    }

  }
}
