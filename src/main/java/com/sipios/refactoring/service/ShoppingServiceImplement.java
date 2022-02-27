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
    private static final String zeroValue = "0";
    @Override
    public String getPrice(Body b) {
        double price = 0;
        double discount;

        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);

        discount = getCustomerDiscount(b);

        // Compute total amount depending on the types and quantity of product and
        // if we are in winter or summer discounts periods
        if ( isNotDiscountPeriod(cal, 5) && isNotDiscountPeriod(cal, 0)) {
            if (b.getItems() == null) {
                return zeroValue;
            }

            price = sumItemsPrice(b, price, discount,false);
        } else {
            if (b.getItems() == null) {
                return zeroValue;
            }

            price = sumItemsPrice(b, price, discount,true);
        }
        sendPriceMessage(b,price);

        return String.valueOf(price);
    }
    double getCustomerDiscount(Body b){
        double toReturn;
        // Compute discount for customer
        boolean isStandardCustomer = checkCustomerType(b, CustomerEnum.STANDARD);
        boolean isPremiumCustomer = checkCustomerType(b, CustomerEnum.PREMIUM);
        boolean isPlatinumCustomer = checkCustomerType(b, CustomerEnum.PLATINUM);
        if (isStandardCustomer) {
            toReturn = 1;
        } else if (isPremiumCustomer) {
            toReturn = 0.9;
        } else if (isPlatinumCustomer) {
            toReturn = 0.5;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return toReturn;
    }
    // throw exceptions with price and customer type
    private String throwPriceException(double price, String customerTypeText) throws Exception {
        throw new Exception("Price (" + price + ") is too high for "+customerTypeText+" customer");
    }
    // check the customer type
    private boolean checkCustomerType(Body b, CustomerEnum type) {
        return b.getType().equals(type.getValue());
    }
    // calculate total price with discount in discount period or not
    public double sumItemsPrice(Body b, double price, double discount, boolean isDiscountPeriod) {
        for (int i = 0; i < b.getItems().length; i++) {
            Item it = b.getItems()[i];

            boolean isTshirt = checkItemType(it, ItemTypeEnum.TSHIRT);
            boolean isDress = checkItemType(it, ItemTypeEnum.DRESS);
            boolean isJacket = checkItemType(it, ItemTypeEnum.JACKET);
            if (isTshirt) {
                price += 30 * it.getNb() * discount;
            } else if (isDress) {
                price += isDiscountPeriod?50 * it.getNb() * 0.8 * discount:50 * it.getNb() * discount;
            } else if (isJacket) {
                price += isDiscountPeriod?100 * it.getNb() * 0.9 * discount:100 * it.getNb() * discount;
            }
            // else if (it.getType().equals("SWEATSHIRT")) {
            //     price += 80 * it.getNb();
            // }
        }
        return price;
    }
    // check the item type
    private boolean checkItemType(Item it, ItemTypeEnum type) {
        return it.getType().equals(type.getValue());
    }
    //check if we are not  in discount period (winter and summmer)
    public boolean isNotDiscountPeriod(Calendar cal, int i) {
        return !(
            cal.get(Calendar.DAY_OF_MONTH) < 15 &&
                cal.get(Calendar.DAY_OF_MONTH) > 5 &&
                cal.get(Calendar.MONTH) == i
        );
    }
    // Send price message
    public void sendPriceMessage(Body b,double price){
        boolean isStandardCustomer = checkCustomerType(b, CustomerEnum.STANDARD);
        boolean isPremiumCustomer = checkCustomerType(b, CustomerEnum.PREMIUM);
        boolean isPlatinumCustomer = checkCustomerType(b, CustomerEnum.PLATINUM);

        try {
            if (isStandardCustomer) {
                if (price > 200) {
                    throwPriceException(price, "standard");
                }
            } else if (isPremiumCustomer) {
                if (price > 800) {
                    throwPriceException(price, "premium");
                }
            } else if (isPlatinumCustomer) {
                if (price > 2000) {
                     throwPriceException(price, "platinum");
                }
            } else {
                if (price > 200) {
                    throwPriceException(price, "standard");
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
