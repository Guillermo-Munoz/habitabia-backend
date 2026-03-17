package com.habitia.shared.domain.valueobject;

import java.time.LocalDate;

public record DateRange(LocalDate checkIn, LocalDate checkOut) {

    public DateRange {
        if (checkIn == null || checkOut == null)
            throw new IllegalArgumentException("Dates cannot be null");
        if (!checkIn.isBefore(checkOut))
            throw new IllegalArgumentException("checkIn must be before checkOut");
    }

    public long nights() {
        return checkIn.until(checkOut).getDays();
    }

    public boolean overlapsWith(DateRange other) {
        return checkIn.isBefore(other.checkOut()) && checkOut.isAfter(other.checkIn());
    }
}