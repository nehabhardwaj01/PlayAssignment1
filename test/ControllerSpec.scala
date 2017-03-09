
import controllers.MyController
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import org.specs2.execute.Results
import org.specs2.mock.Mockito
import play.api.cache.CacheApi
import play.api.mvc.Result
import play.api.test._
import play.api.test.Helpers._
import services.AppCacheProvider
import scala.concurrent.Future

/**
  * Created by neha on 8/3/17.
  */
class ControllerSpec extends PlaySpec with OneAppPerTest with MockitoSugar with Results
{
  "Home Controller" should {

    "render Welcome Page " in {
      val welcome = route(app, FakeRequest(GET, "/")).get
      contentAsString(welcome) must include("Welcome to our Website")
      status(welcome) mustBe OK

    }
  }

  "MyController" should {

    "render Sign In Page" in {
      val mockCA = mock[AppCacheProvider]
      val signIn = new MyController(mockCA)
      val result: Future[Result] = signIn.signInPage().apply(FakeRequest())
      contentAsString(result) must startWith ("<!DOCTYPE html>")

      //result.onComplete { res =>
        result match {
          case Success(html) => html mustBe("text/html")
          case _ =>
        }
      }

      //contentAsString(result)
//
//      val welcome = route(app, FakeRequest(GET, "/signIn")).get
//      contentAsString(welcome) must include("Username")
//      contentAsString(welcome) must include("Password")
//      contentType(welcome) mustBe Some("text/html")
//      status(welcome) mustBe OK

    }

    "render Sign Up Page" in {
      val welcome = route(app, FakeRequest(GET, "/signUp")).get
      contentAsString(welcome) must include("Sign Up Here !!!")
      status(welcome) mustBe OK

    }

  }
}
