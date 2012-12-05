package org.bitbucket.inkytonik.dsprofile

import org.scalatest.{BeforeAndAfter, FunSuite}

class EventsTests extends FunSuite with BeforeAndAfter {

  before {
    Events.profiling = true
    Events.reset
    Events.start( ("one", 1)
                , ("two", 2)
                )
    Events.finish( ("one", 1)
                 , ("two", 2)
                 )
    Events.profiling = false
  }

  after {
    Events.reset
  }
  
  test("length is two"){
    expectResult(2){Events.events.result().length}
  }

  test ("length after reset") {
    Events.reset
    expectResult(0){Events.events.result().length}
  }

}