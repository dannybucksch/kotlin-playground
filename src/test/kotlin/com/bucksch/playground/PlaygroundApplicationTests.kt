package com.bucksch.playground

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration


@SpringBootTest
class PlaygroundApplicationTests {

	@Test
	fun contextLoads() {
		val source = Flux.just("John", "Monica", "Mark", "Cloe", "Frank", "Casper", "Olivia", "Emily", "Cate")
				.filter { name: String -> name.length == 4 }
				.doOnNext { println("on next $it") }
				.map { obj: String -> obj.toUpperCase() }

		source.subscribe{v:String ->println(v)}
		StepVerifier
				.create(source)
				.expectNext("JOHN")
				.expectNextMatches { name: String -> name.startsWith("MA") }
				.expectNext("CLOE", "CATE")
				.expectComplete()
				.verify()
	}
	@Test
	fun heartBeat() {
		val flux = Flux.interval(Duration.ofSeconds(1)).map { HeartBeat(it) }

		println("start")

		flux.take(3)
				.doOnEach { println("on each $it") }
				.map { println("before map");HeartBeat(it.value * 2) }
				.doOnNext { println("on next $it") }
				.doOnComplete { println("on complete") }
				.subscribe { println("subscribe $it") }

		Thread.sleep(5000)
	}

}
data class HeartBeat(val value: Long)
