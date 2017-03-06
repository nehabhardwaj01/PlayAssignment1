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

@Singleton
class SignUpController @Inject() extends Controller{
  val profileController = new ProfileController
  val userForm : Form[SignUpData]= Form(                            //Form is to transform form data into a bound instance of a case class
    mapping(                                      //mapping takes 3 parameters in a curried manner
      "name" -> mapping(
        "firstName" -> nonEmptyText,
        "middleName" -> optional(text),
        "lastName" -> nonEmptyText )(Name.apply)(Name.unapply),
      "gender" -> nonEmptyText,
      "email" -> nonEmptyText,
      "mobileNo" -> number(min = 0,max=Int.MaxValue),
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
        BadRequest(views.html.signUp())
      },
      userData => {
        println(userData)
        profileController.saveData(userData)
        Ok(views.html.profile(userData))
          //.withSession(request.session + "connectedUser" -> userData.username))
      }
    )
  }
}
