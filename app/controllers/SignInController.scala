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
        BadRequest(views.html.signIn(signInData)(""))
      },
      userData => {
        val (exist,message,obj) = validate(userData)
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
    val usernameList= Operations.listOfUser.map(_.username)
    if(usernameList.contains(user.username)){
      val pswd = Operations.listOfUser.filter(_.username==user.username).map(_.password)
      if(pswd == user.password){
        (true,"Sign In Successful",Operations.getUser(user.username))
      }
      else{
        (false,"Incorrect Username or Password",signUpData)
      }
    }
    else{
      (false,"Incorrect Username or Password",signUpData)
    }

  }
}
