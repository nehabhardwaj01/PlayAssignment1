package controllers

import com.google.inject.Singleton
import models.SignUpData
import play.api.mvc._
import scala.collection.mutable.ListBuffer

@Singleton
class ProfileController extends Controller{
  val list : ListBuffer[SignUpData] = ListBuffer()

  def saveData(data : SignUpData) = {
      list += data
    showData()
  }

  def showData() = {
    for(x <- list)
      println(x)
  }

  def getUser(userName : String) : SignUpData ={
   list.filter(_.username.equals(userName)).head
  }

}
