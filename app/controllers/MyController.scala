package controllers

import models.{SignInData, SignUpData, Name}
import play.api.cache.CacheApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Result, Action, Controller}

import javax.inject._
import play.api._
import services.AppCacheProvider

@Singleton
class MyController @Inject()(cache : AppCacheProvider) extends Controller{

  val defaultSignUpData = cache.getDefaultData
  val defaultsignInData = SignInData("","")
//
//  def getName(name:String) = Action {
//    Ok("hello " + name + " !")
//  }
//
  def signInPage = Action{
    Ok(views.html.signIn(defaultsignInData)(""))
  }

  def signUpPage = Action{
    Ok(views.html.signUp(defaultSignUpData)(""))
    }

}
