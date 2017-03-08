package controllers

import play.api.mvc.{Action, Controller}

class ManageUsers extends Controller {
  def manage() = Action{
    Ok(views.html.manageUsers())
  }
}
