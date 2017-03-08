package controllers

import models.{SignInData, SignUpData, Name}
import play.api.cache.CacheApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Result, Action, Controller}

import javax.inject._
import play.api._


case class ButtonType(buttonType: String)

@Singleton
class MyController @Inject()(configuration : play.api.Configuration) extends Controller{

  val present = configuration.underlying.getString("userType")
  val name = new Name(" ",Some(" ")," ")
  val signUpData = if(present.equals("admin")){
     SignUpData(name,"","",0,"","","",0,"",true)
  }
  else {
    SignUpData(name, "", "", 0, "", "", "", 0, "", false)
  }
  val signInData = SignInData("","")

  val userForm = Form(                                //Form is to transform form data into a bound instance of a case class
    mapping(                                          //mapping takes 3 parameters
      "buttonType" -> nonEmptyText
    )(ButtonType.apply)(ButtonType.unapply)
  )

  def getName(name:String) = Action {
    Ok("hello " + name + " !")
  }

  def signInPage = Action{
    Ok(views.html.signIn(signInData)(""))
  }

  def signUpPage = Action{
    Ok(views.html.signUp(signUpData)("")(signUpData.isAdmin))
    }



}
