package controllers
import play.api.Logger
import com.google.inject.{Inject, Singleton}
import play.api.data._
import play.api.libs.json.JsValue
import play.api.mvc.BodyParsers.parse
import play.api.mvc._
import play.api.data.Forms._
import play.api.libs.json._
import models.SignUpData
import models.Name
import services.Operations

@Singleton
class SignUpController @Inject() extends Controller{
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
      "hobbies" -> nonEmptyText
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
          Ok(views.html.profile(userData))
            //.withSession("connectedUser" -> userData.username))
        }
        else {
          Ok(views.html.signUp(userData)(message))
            //.withSession("error" -> message)
        }
      }
    )
  }

  def validate(user : SignUpData) : (Boolean,String)={
    if(user.password == user.rePassword){
      val usernameList= Operations.listOfUser.map(_.username)
      if(usernameList.contains(user.username)){
        (false,"Username already exixts !") }
      else (true, "Sign Up successful!")
    }
    else{
      (false,"Password and password doesnt match !")
    }
    }
}
