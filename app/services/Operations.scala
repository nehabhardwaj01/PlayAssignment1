package services

import models.SignUpData

import scala.collection.mutable.ListBuffer

object Operations {
  val listOfUser : ListBuffer[SignUpData] = ListBuffer()

  def addUser(user:SignUpData) = listOfUser += user

  def getUsers:ListBuffer[SignUpData]= listOfUser

  def getUser(userName : String) : SignUpData ={
    listOfUser.filter(_.username == userName).head
  }

}

