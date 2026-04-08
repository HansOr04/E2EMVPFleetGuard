package com.fleetguard.e2e.screenplay.utils;

import com.fleetguard.e2e.screenplay.notifications.ToastTargets;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Tasks de espera para el componente toast.
 * No usar Thread.sleep() — usar WaitUntil de Serenity.
 */
public class WaitForToast {

    /** Espera hasta 5 segundos a que aparezca el toast. */
    public static Performable toAppear() {
        return Task.where("{0} waits for toast notification to appear",
                WaitUntil.the(ToastTargets.CONTAINER, isVisible())
                        .forNoMoreThan(5).seconds()
        );
    }

    /** Espera hasta 8 segundos a que desaparezca el toast (dura ~4s en producción). */
    public static Performable toDisappear() {
        return Task.where("{0} waits for toast notification to disappear",
                WaitUntil.the(ToastTargets.CONTAINER, isNotVisible())
                        .forNoMoreThan(8).seconds()
        );
    }
}
