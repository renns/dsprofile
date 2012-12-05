package org.bitbucket.inkytonik.dsprofile

import Events.{start, finish}

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers

class ProfilerTests extends Profiler 
                    with FunSuite 
                    with BeforeAndAfter 
                    with ShouldMatchers {

  var otp = ""
  override def output(str: String){ otp = otp + str}
  override def outputln(str: String){ otp = otp + str + "\n"}

  before {
    otp = ""
    startItUp()
    start("won" -> 1, "too" -> 2)
    Thread.sleep(250)
    finish("won" -> 1, "too" -> 2)
    start("won" -> 10, "too" -> 20)
    Thread.sleep(250)
    finish("won" -> 10, "too" -> 20)
    shutItDown(Seq("won"))
  }

  after {
    Events.reset
  }
  
  test ("trivial starter") {
    expectResult(0){0}
  }

  test ("check result") {
    otp should include regex """25\d\s+50\.0\s+25\d\s+50\.0\s+0\s+0\.0\s+1\s+50\.0\s+10""".r
    otp should include regex """25\d\s+50\.0\s+25\d\s+50\.0\s+0\s+0\.0\s+1\s+50\.0\s+1""".r
  }

}