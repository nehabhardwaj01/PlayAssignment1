package controllers

import com.google.inject.{Inject, Singleton}
import play.api.data._
import play.api.mvc._
import play.api.data.Forms._
import models.{Name, SignUpData, SignInData}
import services.{AppCacheProvider, MD5}

@Singleton
class SignInController @Inject()(cache : AppCacheProvider) extends Controller{
  val name = new Name(" ",Some(" ")," ")
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
        val (exist,message) = validate(userData)
        if(exist){
          val obj = cache.getData(userData.username)
          Ok(views.html.profile(obj))
        }
        else{
          Ok(views.html.signIn(userData)(message))
        }
      }
    )
  }

  def validate(user : SignInData) = {
    val userName : SignUpData = cache.getData(user.username)

    if(userName.isEnabled) {
      if(cache.getListOfKeys.contains(user.username)){
        if (userName.password == MD5.hash(user.password)) {
          (true, "Sign In Successful")
        }
        else {
          (false, "Incorrect Password")
        }
      }
        else{
        (false, "Username doesnt exists")
      }
    }
    else {
      (false,"This user is disabled")
    }

  }

}
