package controllers
import play.api.{cache, Logger}
import com.google.inject.{Inject, Singleton}
import play.api.data._
import play.api.data.Forms._
import models.SignUpData
import models.Name
import play.api.cache._
import play.api.mvc._
import services.MD5


@Singleton
class SignUpController @Inject()(cache:CacheApi,configuration: play.api.Configuration) extends Controller{
  val userForm : Form[SignUpData]= Form(                            //Form is to transform form data into a bound instance of a case class
    mapping(                                      //mapping takes 3 parameters in a curried manner
      "name" -> mapping(
        "firstName" -> nonEmptyText,
        "middleName" -> optional(text),
        "lastName" -> nonEmptyText )(Name.apply)(Name.unapply),
      "gender" -> nonEmptyText,
      "email" -> nonEmptyText,
      "mobileNo" -> longNumber(min = Long.MinValue,max=Long.MaxValue),
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "rePassword" -> nonEmptyText,
      "age" -> number(min = 18,max = 75),
      "hobbies" -> nonEmptyText,
      "isAdmin" -> boolean
    )(SignUpData.apply)(SignUpData.unapply)
  )

  def saveContact = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        println(formWithErrors)
        Redirect("/signUp")
      },
      userData => {
        val (exist,message) = validate(userData)
        if(exist==true){
          val hashedPassword = MD5.hash(userData.password)
          val newUser = userData.copy(password = hashedPassword)
          cache.set(userData.username,newUser)
          Ok(views.html.profile(newUser))
        }
        else {
          Ok(views.html.signUp(userData)(message)(userData.isAdmin))
        }
      }
    )
  }

  def getValue(key : String) : Option[SignUpData]= {
    cache.get[SignUpData](key)
  }

  def validate(user : SignUpData) : (Boolean,String)={
    if(user.password == user.rePassword) {
      val userName = getValue(user.username)
      userName match {
        case Some(x) => (false, "Username already exixts !")
        case None => (true, "Sign Up successful!")
      }
    }
    else{
      (false,"Password and password doesnt match !")
    }
    }
}
