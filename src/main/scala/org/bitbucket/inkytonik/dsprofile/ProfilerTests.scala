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

  def countShouldBe = colShouldBe(6)_
  def totalShouldBe = colShouldBe(0)_

  // FIXME: Can't make use of default parameter, compiler complains for reasons I don't understand.
  def colShouldBe(col: Int)(num: Int, dim: String, inp: String, within: Double = 0.0) = {
    import scala.collection.JavaConversions._
    val lines = inp.split("\n")
    lines.foreach {l => 
      val v = l.split("""\s+""")
      if (v.length > 8 && v(8) == dim){
        v(col).toDouble should be < (num + within)
        v(col).toDouble should be > (num - within)
      }
    }
  }

  after {
    Events.reset
  }
  
  test ("check simple result") {
    otp = ""
    profileStart()
    val i = start("won" -> 1, "too" -> 2)
    Thread.sleep(250)
    finish(i)
    val j = start("won" -> 10, "too" -> 20)
    Thread.sleep(250)
    finish(j)
    profileStop(Seq("won"))

    List("1","10").foreach { d => 
      countShouldBe(1, d, otp, 0.0)
      totalShouldBe(250, d, otp, 5.0)
    }
  }

  test ("check slightly more complex result (test b)") {
    otp = ""
    profileStart()
    val i = start("won" -> 1, "too" -> 2)
    Thread.sleep(250)
    finish(i)
    val j = start("won" -> 1, "too" -> 20)
    Thread.sleep(750)
    finish(j)
    val k = start("won" -> 1, "too" -> 20)
    Thread.sleep(750)
    finish(k)
    profileStop(Seq("won"))

    countShouldBe(3, "1", otp, 0.0)
    totalShouldBe(1750, "1", otp, 5.0)
  }

  test ("test b on other dimension") {
    otp = ""
    profileStart()
    val i = start("won" -> 1, "too" -> 2)
    Thread.sleep(250)
    finish(i)
    val k = start("won" -> 1, "too" -> 20)
    Thread.sleep(750)
    finish(k)
    val j = start("won" -> 1, "too" -> 20)
    Thread.sleep(750)
    finish(j)
    profileStop(Seq("too"))

    countShouldBe(1, "2", otp, 0.0)
    totalShouldBe(250, "2", otp, 5.0)

    countShouldBe(2, "20", otp, 0.0)
    totalShouldBe(1500, "20", otp, 5.0)
  }
}