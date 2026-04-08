package com.fleetguard.e2e.screenplay.rules;

/**
 * Tipos de regla de mantenimiento predefinidos en el sistema FleetGuard.
 * Fuente: mockMaintenanceRules del frontend — SOLO estos 51 valores son válidos.
 *
 * <p>El campo "Nombre de la Regla" en /rules es un buscador que filtra esta lista.
 * Si se escribe un valor que no existe aquí, el sistema muestra:
 * "No se encontraron reglas para esta búsqueda" y no permite crear la regla.</p>
 *
 * <p>Cada constante incluye el {@code intervalKm} y {@code warningThresholdKm}
 * que corresponden a los valores del mockMaintenanceRules — se auto-completan al seleccionar.</p>
 */
public enum MaintenanceRuleType {

    // ─── Cambio de aceite ──────────────────────────────────────────────────
    ACEITE_MOTOR_LIVIANO("Cambio de aceite motor liviano",        5000,   500),
    ACEITE_MOTOR_MEDIANO("Cambio de aceite motor mediano",        7000,   700),
    ACEITE_MOTOR_PESADO( "Cambio de aceite motor pesado",        15000,  1500),

    // ─── Filtro de aire ────────────────────────────────────────────────────
    FILTRO_AIRE_LIVIANO( "Cambio de filtro de aire liviano",     10000,  1000),
    FILTRO_AIRE_MEDIANO( "Cambio de filtro de aire mediano",     12000,  1200),
    FILTRO_AIRE_PESADO(  "Cambio de filtro de aire pesado",      20000,  2000),

    // ─── Filtro de aceite ──────────────────────────────────────────────────
    FILTRO_ACEITE_LIVIANO("Cambio de filtro de aceite liviano",   5000,   500),
    FILTRO_ACEITE_MEDIANO("Cambio de filtro de aceite mediano",   7000,   700),
    FILTRO_ACEITE_PESADO( "Cambio de filtro de aceite pesado",   15000,  1500),

    // ─── Filtro de combustible ─────────────────────────────────────────────
    FILTRO_COMBUSTIBLE_LIVIANO("Cambio de filtro de combustible liviano", 20000, 2000),
    FILTRO_COMBUSTIBLE_MEDIANO("Cambio de filtro de combustible mediano", 25000, 2500),
    FILTRO_COMBUSTIBLE_PESADO( "Cambio de filtro de combustible pesado",  40000, 4000),

    // ─── Rotación de llantas ───────────────────────────────────────────────
    ROTACION_LLANTAS_LIVIANO("Rotacion de llantas liviano",  10000, 1000),
    ROTACION_LLANTAS_MEDIANO("Rotacion de llantas mediano",  12000, 1200),
    ROTACION_LLANTAS_PESADO( "Rotacion de llantas pesado",   20000, 2000),

    // ─── Cambio de llantas ─────────────────────────────────────────────────
    CAMBIO_LLANTAS_LIVIANO("Cambio de llantas liviano",  40000, 3000),
    CAMBIO_LLANTAS_MEDIANO("Cambio de llantas mediano",  50000, 4000),
    CAMBIO_LLANTAS_PESADO( "Cambio de llantas pesado",   80000, 8000),

    // ─── Pastillas de freno ────────────────────────────────────────────────
    PASTILLAS_FRENO_LIVIANO("Cambio de pastillas de freno liviano", 20000, 2000),
    PASTILLAS_FRENO_MEDIANO("Cambio de pastillas de freno mediano", 25000, 2500),
    PASTILLAS_FRENO_PESADO( "Cambio de pastillas de freno pesado",  30000, 3000),

    // ─── Discos de freno ───────────────────────────────────────────────────
    DISCOS_FRENO_LIVIANO("Cambio de discos de freno liviano", 40000, 3000),
    DISCOS_FRENO_MEDIANO("Cambio de discos de freno mediano", 50000, 4000),
    DISCOS_FRENO_PESADO( "Cambio de discos de freno pesado",  60000, 5000),

    // ─── Batería ───────────────────────────────────────────────────────────
    BATERIA_LIVIANA("Revision de bateria liviano", 15000, 1500),
    BATERIA_MEDIANA("Revision de bateria mediano", 20000, 2000),
    BATERIA_PESADA( "Revision de bateria pesado",  30000, 3000),

    // ─── Refrigerante ──────────────────────────────────────────────────────
    REFRIGERANTE_LIVIANO("Cambio de refrigerante liviano", 40000, 4000),
    REFRIGERANTE_MEDIANO("Cambio de refrigerante mediano", 50000, 5000),
    REFRIGERANTE_PESADO( "Cambio de refrigerante pesado",  80000, 8000),

    // ─── Aceite de transmisión ─────────────────────────────────────────────
    TRANSMISION_LIVIANA("Cambio de aceite de transmision liviano",  40000,  4000),
    TRANSMISION_MEDIANA("Cambio de aceite de transmision mediano",  60000,  6000),
    TRANSMISION_PESADA( "Cambio de aceite de transmision pesado",  100000, 10000),

    // ─── Aire acondicionado ────────────────────────────────────────────────
    AIRE_AC_LIVIANO("Mantenimiento de aire acondicionado liviano", 20000, 2000),
    AIRE_AC_MEDIANO("Mantenimiento de aire acondicionado mediano", 25000, 2500),
    AIRE_AC_PESADO( "Mantenimiento de aire acondicionado pesado",  30000, 3000),

    // ─── Alineación y balanceo ─────────────────────────────────────────────
    ALINEACION_LIVIANO("Alineacion y balanceo liviano", 10000, 1000),
    ALINEACION_MEDIANO("Alineacion y balanceo mediano", 12000, 1200),
    ALINEACION_PESADO( "Alineacion y balanceo pesado",  20000, 2000),

    // ─── Suspensión ────────────────────────────────────────────────────────
    SUSPENSION_LIVIANA("Revision de suspension liviano", 30000, 3000),
    SUSPENSION_MEDIANA("Revision de suspension mediano", 40000, 4000),
    SUSPENSION_PESADA( "Revision de suspension pesado",  60000, 6000),

    // ─── Sistema eléctrico ─────────────────────────────────────────────────
    ELECTRICO_LIVIANO("Revision sistema electrico liviano", 20000, 2000),
    ELECTRICO_MEDIANO("Revision sistema electrico mediano", 25000, 2500),
    ELECTRICO_PESADO( "Revision sistema electrico pesado",  40000, 4000),

    // ─── Bujías ────────────────────────────────────────────────────────────
    BUJIAS_LIVIANO("Cambio de bujias liviano", 30000, 3000),
    BUJIAS_MEDIANO("Cambio de bujias mediano", 40000, 4000),
    BUJIAS_PESADO( "Cambio de bujias pesado",  60000, 6000),

    // ─── Limpiaparabrisas ──────────────────────────────────────────────────
    PLUMILLAS_LIVIANO("Cambio de plumillas limpiaparabrisas liviano", 10000, 1000),
    PLUMILLAS_MEDIANO("Cambio de plumillas limpiaparabrisas mediano", 12000, 1200),
    PLUMILLAS_PESADO( "Cambio de plumillas limpiaparabrisas pesado",  15000, 1500);

    // ──────────────────────────────────────────────────────────────────────

    private final String ruleType;
    private final int intervalKm;
    private final int warningThresholdKm;

    MaintenanceRuleType(String ruleType, int intervalKm, int warningThresholdKm) {
        this.ruleType = ruleType;
        this.intervalKm = intervalKm;
        this.warningThresholdKm = warningThresholdKm;
    }

    /** Nombre exacto del tipo de regla — debe coincidir con mockMaintenanceRules.ruleType. */
    public String ruleType() { return ruleType; }

    /** Intervalo en km definido por el mock — se auto-completa al seleccionar en /rules. */
    public int intervalKm() { return intervalKm; }

    /** Umbral de aviso en km definido por el mock. */
    public int warningThresholdKm() { return warningThresholdKm; }

    @Override
    public String toString() { return ruleType; }
}
