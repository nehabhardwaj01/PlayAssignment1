package controllers

import com.google.inject.{Inject, Singleton}
import play.api.cache.CacheApi
import play.api.data._
import play.api.mvc._
import play.api.data.Forms._
import models.{Name, SignUpData, SignInData}
import services.MD5

@Singleton
class SignInController @Inject()(cache : CacheApi) extends Controller{
  val name = new Name(" ",Some(" ")," ")
  val signUpData = SignUpData(name,"","",0,"","","",0,"",false)
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

  def getValue(key : String) : Option[SignUpData]= {
    cache.get[SignUpData](key)
  }

  def validate(user : SignInData) = {
    val userName = getValue(user.username)

    userName match {
      case Some(x) => {
        if (x.password == MD5.hash(user.password)) {
          (true, "Sign In Successful", x)
        }
        else {
          (false, "Incorrect Password", signUpData)
        }
      }
      case None => (false, "Username doesnt exists", signUpData)
    }
  }

}
