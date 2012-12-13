package org.bitbucket.inkytonik.dsprofile

import org.scalatest.{BeforeAndAfter, FunSuite}

class EventsTests extends FunSuite with BeforeAndAfter {

  before {
    Events.profiling = true
    Events.reset
    val i = Events.start ( ("one", 1)
                         , ("two", 2)
                         )
    Events.finish (i)
    Events.profiling = false
  }

  after {
    Events.reset
  }
  
  test ("length is two"){
    expectResult (2) {Events.events.result ().length}
  }

  test ("length after reset") {
    Events.reset
    expectResult (0) {Events.events.result ().length}
  }

  test ("test event ids are unique") {
    expectResult (1000) {
      ((((1 to 1000).toList).map {i => Events.start ()}).toSet).size
    }
  }

}