package com.fleetguard.e2e.screenplay.notifications;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Targets del toast global de FleetGuard.
 * El toast es un div position:fixed bottom-right que solo existe en el DOM cuando está visible.
 * Dura ~4 segundos antes de desaparecer.
 * Fuente: SELECTORS.md sección 7 — Componentes Globales.
 */
public class ToastTargets {

    public static final Target CONTAINER = Target.the("Toast container")
            .locatedBy("//*[contains(@class,'fixed') and contains(@class,'bottom') " +
                    "and contains(@class,'right') and contains(@class,'rounded')]");

    public static final Target MESSAGE = Target.the("Toast mensaje de texto")
            .locatedBy("//*[contains(@class,'fixed') and contains(@class,'bottom')]//p");

    public static final Target SUCCESS_ICON = Target.the("Toast icono éxito (check_circle)")
            .locatedBy("//*[contains(@class,'fixed') and contains(@class,'bottom')]" +
                    "//span[normalize-space()='check_circle']");

    public static final Target ERROR_ICON = Target.the("Toast icono error (cancel)")
            .locatedBy("//*[contains(@class,'fixed') and contains(@class,'bottom')]" +
                    "//span[normalize-space()='cancel']");
}
