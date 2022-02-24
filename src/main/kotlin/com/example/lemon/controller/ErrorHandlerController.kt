package com.example.lemon.controller
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * ErrorHandler for the application.
 *
 * @author	Agustin Albiero
 * @since	v1.0.0
 * @version	v1.0.0	Thursday, February 17th, 2022.
 */
@RestController
@RequestMapping("/")
class ErrorHandlerController: ErrorController {

    /**
     * Called whenever a wrong endpoint is called but no other error handler is found
     *
     * @author	Agustin Albiero
     * @since	v1.0.0
     * @version	v1.0.0	Thursday, February 17th, 2022.
     * @return	error message
     */
    @RequestMapping(value = ["/error"])
    fun getErrorPath(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Only /getFoaas endpoint is supported")
    }

}