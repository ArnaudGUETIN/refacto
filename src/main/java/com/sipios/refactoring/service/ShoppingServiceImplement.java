package com.sipios.refactoring.service;

import com.sipios.refactoring.controller.entities.Body;
import com.sipios.refactoring.controller.entities.Item;
import com.sipios.refactoring.enums.CustomerEnum;
import com.sipios.refactoring.enums.ItemTypeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
@Service
public class ShoppingServiceImplement implements ShoppingService{
    @Override
    public String getPrice(Body b) {
        double p = 0;
        double d;

        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);

        // Compute discount for customer
        if (b.getType().equals(CustomerEnum.STANDARD.getValue())) {
            d = 1;
        } else if (b.getType().equals(CustomerEnum.PREMIUM.getValue())) {
            d = 0.9;
        } else if (b.getType().equals(CustomerEnum.PLATINUM.getValue())) {
            d = 0.5;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // Compute total amount depending on the types and quantity of product and
        // if we are in winter or summer discounts periods
        if (
            !(
                cal.get(Calendar.DAY_OF_MONTH) < 15 &&
                    cal.get(Calendar.DAY_OF_MONTH) > 5 &&
                    cal.get(Calendar.MONTH) == 5
            ) &&
                !(
                    cal.get(Calendar.DAY_OF_MONTH) < 15 &&
                        cal.get(Calendar.DAY_OF_MONTH) > 5 &&
                        cal.get(Calendar.MONTH) == 0
                )
        ) {
            if (b.getItems() == null) {
                return "0";
            }

            for (int i = 0; i < b.getItems().length; i++) {
                Item it = b.getItems()[i];

                if (it.getType().equals(ItemTypeEnum.TSHIRT.getValue())) {
                    p += 30 * it.getNb() * d;
                } else if (it.getType().equals(ItemTypeEnum.DRESS.getValue())) {
                    p += 50 * it.getNb() * d;
                } else if (it.getType().equals(ItemTypeEnum.JACKET.getValue())) {
                    p += 100 * it.getNb() * d;
                }
                // else if (it.getType().equals("SWEATSHIRT")) {
                //     price += 80 * it.getNb();
                // }
            }
        } else {
            if (b.getItems() == null) {
                return "0";
            }

            for (int i = 0; i < b.getItems().length; i++) {
                Item it = b.getItems()[i];

                if (it.getType().equals(ItemTypeEnum.TSHIRT.getValue())) {
                    p += 30 * it.getNb() * d;
                } else if (it.getType().equals(ItemTypeEnum.DRESS.getValue())) {
                    p += 50 * it.getNb() * 0.8 * d;
                } else if (it.getType().equals(ItemTypeEnum.JACKET.getValue())) {
                    p += 100 * it.getNb() * 0.9 * d;
                }
                // else if (it.getType().equals("SWEATSHIRT")) {
                //     price += 80 * it.getNb();
                // }
            }
        }

        try {
            if (b.getType().equals(CustomerEnum.STANDARD.getValue())) {
                if (p > 200) {
                    throw new Exception("Price (" + p + ") is too high for standard customer");
                }
            } else if (b.getType().equals(CustomerEnum.PREMIUM.getValue())) {
                if (p > 800) {
                    throw new Exception("Price (" + p + ") is too high for premium customer");
                }
            } else if (b.getType().equals(CustomerEnum.PLATINUM.getValue())) {
                if (p > 2000) {
                    throw new Exception("Price (" + p + ") is too high for platinum customer");
                }
            } else {
                if (p > 200) {
                    throw new Exception("Price (" + p + ") is too high for standard customer");
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return String.valueOf(p);
    }
}