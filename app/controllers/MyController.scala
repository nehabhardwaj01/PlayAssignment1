package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Result, Action, Controller}

import javax.inject._
import play.api._


case class ButtonType(buttonType: String)

@Singleton
class MyController @Inject() extends Controller{
  val userForm = Form(                                //Form is to transform form data into a bound instance of a case class
    mapping(                                          //mapping takes 3 parameters
      "buttonType" -> nonEmptyText
    )(ButtonType.apply)(ButtonType.unapply)
  )

  def getName(name:String) = Action {
    Ok("hello " + name + " !")
  }

  def signInPage = Action{
    Ok(views.html.signIn())
  }

  def signUpPage = Action{
    Ok(views.html.signUp())
  }



}
