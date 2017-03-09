package controllers

import com.google.inject.Inject
import models.SignUpData
import play.api.cache.CacheApi
import play.api.mvc.{Action, Controller}
import play.api.mvc._
import javax.inject._
import play.api._
import play.api.mvc._
import services.AppCacheProvider

@Singleton
class ManageUsers @Inject()(cache:AppCacheProvider) extends Controller {

  def manage = Action{
    Ok(views.html.manageUsers(cache.getListOfUsers))
  }

  def enable(username : String) = Action{
    val newUser = cache.getData(username).copy(isEnabled = true)
    cache.setData(username,newUser)
    Ok(views.html.manageUsers(cache.getListOfUsers))
  }

  def disable(username : String) = Action{
    val newUser = cache.getData(username).copy(isEnabled = false)
    cache.setData(username,newUser)
    Ok(views.html.manageUsers(cache.getListOfUsers))
  }


}
