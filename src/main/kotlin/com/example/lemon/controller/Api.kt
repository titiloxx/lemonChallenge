package com.example.lemon.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
class Api {
    
    @RequestMapping("/getFoaas")
    fun getFoaas(@RequestHeader("userid") userId:String): ResponseEntity<String> {
        val foaasResponse = ClientService.getFoaasResponse(userId)
        return ResponseEntity(foaasResponse, HttpStatus.OK)

    }

    @ExceptionHandler
    fun exceptionHandler(ex: Exception): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
