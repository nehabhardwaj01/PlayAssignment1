package controllers

import com.google.inject.Singleton
import play.api.data._
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.data.Forms._
import models.{SignUpData, SignInData}
import services.Operations

@Singleton
class SignInController extends Controller{

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
        val user = Operations.getUser(userData.username)
        if((user.username == userData.username) && (user.password == userData.password))
          Ok(views.html.profile(user)).withSession("connected" -> userData.username)
        else
          BadRequest(views.html.signIn())
      }
    )
  }


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
