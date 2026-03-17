package com.habitia.shared.domain.valueobject;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
    public Money{
        if(amount == null || amount.compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("Amount must be positive");
        if(currency == null || currency.isBlank())
                throw new IllegalArgumentException("Currency cannot be blank");

    }
    public static Money of(BigDecimal amount, String currency){
        return new Money(amount, currency);
    }
    public static Money euro(BigDecimal amount){
        return new Money(amount, "EUR");
    }

}
