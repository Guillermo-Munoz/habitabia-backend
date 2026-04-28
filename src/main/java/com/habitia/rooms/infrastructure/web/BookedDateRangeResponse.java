package com.habitia.rooms.infrastructure.web;

import java.time.LocalDate;

public record BookedDateRangeResponse(
        LocalDate checkIn,
        LocalDate checkOut
) {}
