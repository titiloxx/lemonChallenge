package com.example.lemon.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


/**
 * Api controller has all the endpoints for the api
 *
 * @author	Agustin Albiero
 * @since	v1.0.0
 * @version	v1.0.0	Thursday, February 17th, 2022.
 */
@RestController
@RequestMapping("/")
class ApiController {

    /**
     * Fetches a message from FOAAS API only if allowed.
     *
     * @author	Agustin Albiero
     * @since	v1.0.0
     * @version	v1.0.0	Thursday, February 17th, 2022.
     * @param userId the user id passed as a header called userId
     * @return the response from the FOAAS service
     */
    @RequestMapping("getFoaas")
    fun getFoaas(@RequestHeader("userid") userId:String): ResponseEntity<String> {
        val foaasResponse = ClientService.getFoaasResponse(userId)
        return ResponseEntity(foaasResponse, HttpStatus.OK)
    }

    /**
     * Handles exceptions thrown by ApiController.
     *
     * @author	Agustin Albiero
     * @since	v1.0.0
     * @version	v1.0.0	Thursday, February 17th, 2022.
     * @param   ex the exception thrown
     * @return  exception message
     */
    @ExceptionHandler
    fun exceptionHandler(ex: Exception): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
