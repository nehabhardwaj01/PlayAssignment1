package controllers
import com.google.inject.{Inject, Singleton}
import play.api.data._
import play.api.data.Forms._
import models.SignUpData
import models.Name
import play.api.mvc._
import services.{AppCacheProvider, MD5}


@Singleton
class SignUpController @Inject()(cache:AppCacheProvider) extends Controller{
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
      "isAdmin" -> boolean,
      "isEnabled" -> boolean
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
          val newUser = updateCache(userData)
          Ok(views.html.profile(newUser))
        }
        else {
          Ok(views.html.signUp(userData)(message))
        }
      }
    )
  }

  def validate(user : SignUpData) : (Boolean,String)={
    if(user.password.equals(user.rePassword)) {
      val userName = cache.getData(user.username)
      if(cache.getListOfKeys.contains(userName)) {
        (false, "Username already exixts !")
      }
      else{
        (true, "Sign Up successful!")
      }
    }
    else{
      (false,"Password and Re-Password doesnt match !")
    }
  }

  def updateCache(userData : SignUpData ) : SignUpData = {
    val hashedPassword = MD5.hash(userData.password)
    val newUser = userData.copy(password = hashedPassword,rePassword = hashedPassword)
    cache.setData(userData.username,newUser)
    cache.updateListOfKeys(userData.username)
    newUser

  }
}
