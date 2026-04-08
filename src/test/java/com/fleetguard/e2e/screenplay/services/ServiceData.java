package com.fleetguard.e2e.screenplay.services;

/**
 * Datos de un servicio de mantenimiento a registrar.
 * performedAt: formato "yyyy-MM-dd" (input type=date).
 */
public record ServiceData(
        String plate,
        String recordedBy,
        String performedAt,
        String provider,
        String cost,
        String description,
        String mileageAtService
) {}
