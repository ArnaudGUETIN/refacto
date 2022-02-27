package com.sipios.refactoring.controller;

import com.sipios.refactoring.UnitTest;
import com.sipios.refactoring.controller.entities.Body;
import com.sipios.refactoring.controller.entities.Item;
import com.sipios.refactoring.service.ShoppingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShoppingControllerTests extends UnitTest {

    @Autowired
    private ShoppingService shoppingService;

    @Test
    void should_not_throw() {
        Assertions.assertDoesNotThrow(
            () -> shoppingService.getPrice(new Body(new Item[] {}, "STANDARD_CUSTOMER"))
        );
    }
}
