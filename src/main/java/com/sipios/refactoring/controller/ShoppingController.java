package com.sipios.refactoring.controller;



import com.sipios.refactoring.controller.entities.Body;
import com.sipios.refactoring.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    @PostMapping
    public ResponseEntity<String> getPrice(@RequestBody Body b) {
        return new ResponseEntity<String>(shoppingService.getPrice(b),HttpStatus.OK);
    }

}




