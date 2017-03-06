package controllers

import com.google.inject.Singleton
import play.api.data._
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.data.Forms._
import models.{SignUpData, SignInData}

@Singleton
class SignInController extends Controller{
  val signUpController = new SignUpController

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
        BadRequest(views.html.signIn())
      },
      userData => {
        if(!validate(userData)) {
          Ok("Client Error")
        }
        else {
          Ok(views.html.profile(signUpController.profileController.getUser(userData.username)))
        }
      }
    )
  }

  def validate(userData : SignInData) : Boolean= {
    if (signUpController.profileController.list.contains(userData.username)) {
      (signUpController.profileController.getUser(userData.username).password).equals(userData.password)
    }
    else {
      false
    }
  }
//
//  Ok("Welcome!").withSession(
//    "connected" -> "user@gmail.com")
//
//
//  def index = Action { request =>
//    request.session.get("connectedUser").map { user =>
//      Ok("Hello " + user)
//    }.getOrElse {
//      Unauthorized("Oops, you are not connected")
//    }
//  }


}
