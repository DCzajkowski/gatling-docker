package sample

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import java.net.InetAddress

class SampleService extends Simulation {

  // val httpProtocol = http
	// 	.baseURL("http://63.34.148.153:80")
	// 	.acceptHeader("*/*")

  // val myaddress = InetAddress.getLocalHost.toString

	// val scn = scenario("Sample Service Root")
	// 	.exec(http("request_1")
	// 	.get("/boards"))

	// setUp(
  //   scn.inject(
	// 		nothingFor(2 seconds)
  //     // , atOnceUsers(10)
	// 		// , nothingFor(4 seconds)
  //     , rampUsers(10) over(10 seconds)
	// 		, nothingFor(5 seconds)
  //     // , rampUsers(100) over(10 seconds)
	// 		// , nothingFor(5 seconds)
  //     , rampUsersPerSec(30) to 300 during(10 minutes)
  //   )
  // ).protocols(httpProtocol)

	// setUp(
  //   scn.inject(
	// 		nothingFor(5 seconds),
	// 		constantUsersPerSec(150) during(100 minutes)
  //   )
  // ).protocols(httpProtocol)

  val httpProtocol = http
    .baseUrl("http://63.34.148.153:80")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Gatling2")
    .wsBaseUrl("ws://63.34.148.153:80")

  // .exec(http("Home").get("/"))
  // .pause(1)
  // .exec(session => session.set("id", "Steph" + session.userId))
  // .exec(http("Login").get("/room?username=${id}"))
  // .pause(1)
  // .exec(ws("Message").sendText("""["1","1","board:1","phx_join",{}]"""))

  val scn = scenario("WebSocket")
    .exec(
      ws("Connect")
        .connect("/socket/websocket?token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJ0aGVzaXMiLCJleHAiOjE1Nzk4ODY1MTcsImlhdCI6MTU3NzQ2NzMxNywiaXNzIjoidGhlc2lzIiwianRpIjoiYmYwMjU4YjEtZWI1YS00M2M4LWIxMTctMDM5NjNkMGIzYjcyIiwibmJmIjoxNTc3NDY3MzE2LCJzdWIiOiIxIiwidGhlc2lzIjp7InRlYW0iOnsiYm9hcmRfaWRzIjpbMV0sImlkIjoxfX0sInR5cCI6ImFjY2VzcyJ9.Sp_dsKXED_6kUXx16itDClsjnIGVQtqhf-_joImYlDiVFPHekK-6M1WScHMbkPU6p2daiZgGrHHDxwo9eDWUTg")
        .onConnected(
          exec(
            ws("Channel")
              .sendText("""{"topic":"board:1", "event":"phx_join", "payload": {}, "ref":"1"}""")
          )
        )
    )
    .pause(10000)
    .exec(ws("Close WS").close)

    setUp(
      scn.inject(
        nothingFor(5 seconds)
        // , constantUsersPerSec(5) during(100 minutes)
        // , rampUsersPerSec(50) to 1000 during(10 minutes)
        rampConcurrentUsers(50) to (1000) during(1 minutes)
      )
    ).protocols(httpProtocol)
}
