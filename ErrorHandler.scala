import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent._
import javax.inject.Singleton;

@Singleton
class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      statusCode match {

        case 400 => (Status(statusCode)("Bad Request !!!"))
        case 404 => (Status(statusCode)("Page not found error!!!"))
        case 403 => (Status(statusCode)("Forbidden!!!"))
        case 200 => (Status(statusCode)("Ok!!!"))
        case 201 => (Status(statusCode)("Ok Created!!!"))
        case 202 => (Status(statusCode)("Accepted!!!"))
        case _ => (Status(statusCode)("Some error Occured"))
      }
    )

  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }
}