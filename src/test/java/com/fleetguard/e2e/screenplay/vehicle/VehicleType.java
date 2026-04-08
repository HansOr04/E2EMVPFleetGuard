package com.fleetguard.e2e.screenplay.vehicle;

/**
 * Tipos de vehículo válidos definidos en la tabla vehicle_type del backend.
 * Corresponden al texto visible del select en /register y los botones toggle en /rules.
 * Fuente: INSERT INTO vehicle_type — 15 registros fijos (UUIDs inmutables).
 */
public enum VehicleType {

    SEDAN("Sedán"),
    HATCHBACK("Hatchback"),
    SUV("SUV"),
    CROSSOVER("Crossover"),
    PICKUP("Pickup"),
    COUPE("Coupé"),
    CONVERTIBLE("Convertible"),
    MINIVAN("Minivan"),
    VAN("Van"),
    MOTOCICLETA("Motocicleta"),
    CAMION("Camión"),
    AUTOBUS("Autobús"),
    MICROBUS("Microbús"),
    FURGON("Furgón"),
    TRACTOR("Tractor");

    private final String visibleText;

    VehicleType(String visibleText) {
        this.visibleText = visibleText;
    }

    /** Texto visible en el dropdown de /register y en los botones toggle de /rules. */
    public String text() {
        return visibleText;
    }

    @Override
    public String toString() {
        return visibleText;
    }
}
