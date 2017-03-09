package services

import com.google.inject.Inject
import models.{Name, SignUpData}
import play.api.cache._;

trait AppCache{

  def getData(username : String) : SignUpData
  def setData(key :String, value : SignUpData)

  def getListOfKeys : List[String]
  def updateListOfKeys(username : String)

  def getDefaultData  :SignUpData
  def setDefault

  def getListOfUsers : List[SignUpData]

}


class AppCacheProvider @Inject()(cache : CacheApi, configuration : play.api.Configuration) extends AppCache {
  override def getData(username : String) : SignUpData= {
    cache.get[SignUpData](username) match{
      case Some(x) => x
      case None => getDefaultData
    }
  }

  override def setData(username :String,user : SignUpData) = {
    cache.set(username, user)

  }

  override  def getListOfKeys : List[String]= {
    cache.get[List[String]]("listOfKeys") match{
      case Some(x) =>x
      case None => Nil
    }
  }


  override def updateListOfKeys(username : String) = {
    cache.set("listOfKeys" , getListOfKeys :+ username)
  }


  override def getDefaultData : SignUpData = {
    setDefault
    cache.get[SignUpData]("defaultData") match{
      case Some(x) => x
    }
  }

  def getListOfUsers : List[SignUpData] ={
    val listOfKeys = getListOfKeys

    val listOfUsers : List[SignUpData] = for{
      key <- listOfKeys
      user = getData(key)
    }yield user

    listOfUsers
  }

  def setDefault = {
    val name = new Name(" ",Some(" ")," ")
    val present = configuration.underlying.getString("userType")
    if(present.equalsIgnoreCase("admin")){
      setData("defaultData" , SignUpData(name,"","",0,"","","",0,"",true))
    }
    else {
      setData("defaultData" , SignUpData(name,"","",0,"","","",0,"",false))
    }
  }

}