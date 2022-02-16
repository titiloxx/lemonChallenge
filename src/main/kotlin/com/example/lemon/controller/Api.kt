package com.example.lemon.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class Api {
    
    @RequestMapping("getFoaas")
    fun getFoaas(@RequestHeader("userid") userId:String): ResponseEntity<String> {
        val foaasResponse = ClientService.getFoaasResponse(userId)
        return ResponseEntity(foaasResponse, HttpStatus.OK)

    }
    @RequestMapping("error", method = arrayOf(RequestMethod.GET))
    fun error(): ResponseEntity<String> = ResponseEntity("Only /getFoaas path is supported", HttpStatus.BAD_REQUEST)

   @ExceptionHandler
    fun exceptionHandler(ex: Exception): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
