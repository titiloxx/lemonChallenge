package com.example.lemon

import com.example.lemon.controller.Api
import org.junit.AfterClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
properties = ["spring.datasource.url=jdbc:h2:mem:testdb"])

class LemonApplicationTests(@Autowired val client: TestRestTemplate) {


	/**
	 * Test to check if error message appears after trying to access the endpoint
	 * more than MAX_RETRIES within a MAX_TIME time window
	 *
	 * @author	Agustin Albiero
	 * @since	v1.0.0
	 * @version	v1.0.0	Thursday, February 17th, 2022.
	 * @return	void
	 */
	@Test
	fun sixTimeFailure() {
		val headers = HttpHeaders();
		headers.set("userId", "testuserid");
		val requestHeader = HttpEntity<String>(headers);
		for (i in 1..ClientService.MAX_RETRIES) {
			val response = client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
			assert(response.body!!.contains("FOAAS - Fuck this"))
		}
		val lastResponse = client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)

		println(lastResponse.body)
		assert(lastResponse.body != null)
		assert(lastResponse.body!!.contains("You can only fetch five times every 10 seconds"))
	}
	
	/**
	 * Test to check if error message is returned when no userid is provided
	 *
	 * @author	Agustin Albiero
	 * @since	v1.0.0
	 * @version	v1.0.0	Thursday, February 17th, 2022.
	 * @return	void
	 */
	@Test
	fun userIdNotPresent() {
		val response = client.getForObject("/getFoaas", String::class.java)
		assert(response.contains("Required request header 'userid'"))
	}

	
	/**
	 * Test to check if FOAAS response is returned when userid is provided
	 *
	 * @author	Agustin Albiero
	 * @since	v1.0.0
	 * @version	v1.0.0	Thursday, February 17th, 2022.
	 * @return	void
	 */
	@Test
	fun userIdPresent() {
		val headers = HttpHeaders();
		headers.set("userId", "123456");
		val requestHeader = HttpEntity<String>(headers);

		val response=client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		assert(response.body != null)
		assert(response.body!!.contains("FOAAS - Fuck this"))
	}


	/**
	 * Test to check if access to wrong endpoint returns error message
	 *
	 * @author	Agustin Albiero
	 * @since	v1.0.0
	 * @version	v1.0.0	Thursday, February 17th, 2022.
	 * @return	void
	 */
	@Test
	fun wrongEndpoint() {
		val headers = HttpHeaders();
		headers.set("userId", "123456");
		val requestHeader = HttpEntity<String>(headers);

		val response=client.exchange("/getFoaasWrong", HttpMethod.GET, requestHeader, String::class.java)
		assert(response.body!=null)
		assert(response.body!!.contains("Only /getFoaas path is supported"))
	}


	/**
	 * Test to check if after waiting MAX_TIME time window, the endpoint is available again
	 *
	 * @author	Agustin Albiero
	 * @since	v1.0.0
	 * @version	v1.0.0	Thursday, February 17th, 2022.
	 * @return	void
	 */
	@Test
	fun waitMaxTimeAndRetry() {
		Thread.sleep(ClientService.MAX_TIME)
		val headers = HttpHeaders();
		headers.set("userId", "123456");
		val requestHeader = HttpEntity<String>(headers);
		val response = client.exchange("/getFoaas", HttpMethod.GET, requestHeader, String::class.java)
		assert(response.body!=null)
		assert(response.body!!.contains("FOAAS - Fuck this"))
	}

}
