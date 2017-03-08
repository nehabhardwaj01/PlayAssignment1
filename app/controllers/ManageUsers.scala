package controllers

import com.google.inject.Inject
import models.SignUpData
import play.api.cache.CacheApi
import play.api.mvc.{Action, Controller}
import play.api.mvc._
import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class ManageUsers @Inject()(cache:CacheApi) extends Controller {

  def manage = Action{
    Ok(views.html.manageUsers(getListOfUsers))
  }

  def getListOfUsers ={
    val listOfKeys = cache.get[List[String]]("listOfKeys") match{
      case Some(x) => x
      case None => Nil
    }

    val listOfUsers : List[SignUpData] = for{
                          key <- listOfKeys
                          user = cache.get[SignUpData](key) match{
                            case Some(x) => x
                          }
                        }yield user
    listOfUsers
  }
}
