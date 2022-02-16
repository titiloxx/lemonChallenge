package com.example.lemon

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
properties = ["spring.datasource.url=jdbc:h2:mem:testdb"])

class LemonApplicationTests(@Autowired val client:TestRestTemplate) {

	@Test
	fun userIdNotPresent() {
		val response=client.getForObject("/getFoaas", String::class.java)
		assert(response.contains("Required request header 'userid'"))
	}

	@Test
	fun userIdPresent() {
		val headers = HttpHeaders();
		headers.set("userId", "123456");
		val requestHeader = HttpEntity<String>(headers);

		val response=client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		assert(response.body!=null)
		assert(response.body!!.contains("FOAAS - Fuck this"))
	}

	@Test
	fun sixTimeFailure() {
		val headers = HttpHeaders();
		headers.set("userId", "123456");
		val requestHeader = HttpEntity<String>(headers);

		client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		val lastResponse=client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)

		println(lastResponse.body)
		assert(lastResponse.body!=null)
		assert(lastResponse.body!!.contains("You can only fetch five times every 10 seconds"))
		// wait 10 secs
		Thread.sleep(10000)

		val successResponse=client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		assert(successResponse.body!=null)
		assert(successResponse.body!!.contains("FOAAS - Fuck this"))
	}
}
