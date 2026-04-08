package com.fleetguard.e2e.screenplay.rules;

/**
 * Tipos de mantenimiento válidos del select en /rules.
 * El value del &lt;option&gt; es PREVENTIVE/CORRECTIVE pero se selecciona
 * por texto visible con SelectFromOptions.byVisibleText().
 */
public enum MaintenanceType {

    PREVENTIVO("Preventivo"),
    CORRECTIVO("Correctivo");

    private final String visibleText;

    MaintenanceType(String visibleText) {
        this.visibleText = visibleText;
    }

    /** Texto visible que aparece en el dropdown select[@name='maintenanceType']. */
    public String text() {
        return visibleText;
    }

    @Override
    public String toString() {
        return visibleText;
    }
}
