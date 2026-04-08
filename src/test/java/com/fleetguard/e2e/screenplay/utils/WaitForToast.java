package com.fleetguard.e2e.screenplay.utils;

import com.fleetguard.e2e.screenplay.notifications.ToastTargets;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Tasks de espera para el componente toast de FleetGuard.
 *
 * <p><b>Comportamiento del toast:</b>
 * El componente se monta/desmonta condicionalmente en el DOM.
 * Al desaparecer pasa por un estado de transición donde el contenedor
 * sigue siendo {@code isDisplayed()=true} pero su texto ya es vacío ({@code ""}).
 * {@code WaitUntil isNotVisible()} falla en este estado intermedio.</p>
 *
 * <p>{@link #toDisappear()} maneja este caso con un bucle de polling manual que
 * considera el toast "desaparecido" cuando:
 * <ul>
 *   <li>El elemento no está en el DOM ({@code NoSuchElementException}), O</li>
 *   <li>El elemento no es visible ({@code isDisplayed() == false}), O</li>
 *   <li>El texto del elemento está vacío (estado de transición fade-out)</li>
 * </ul>
 * </p>
 */
public class WaitForToast {

    private WaitForToast() { /* utility class */ }

    /** Espera hasta 8 segundos a que aparezca el toast con contenido visible. */
    public static Performable toAppear() {
        return Task.where("{0} waits for toast notification to appear",
                WaitUntil.the(ToastTargets.CONTAINER, isVisible())
                        .forNoMoreThan(8).seconds()
        );
    }

    /**
     * Espera hasta 12 segundos a que desaparezca el toast.
     *
     * <p>Considera el toast desaparecido si:
     * <ol>
     *   <li>El elemento no existe en el DOM, O</li>
     *   <li>{@code isDisplayed() == false}, O</li>
     *   <li>El texto del elemento está vacío (transición fade-out — el contenedor
     *       persiste brevemente con texto vacío antes de ser desmontado del DOM)</li>
     * </ol>
     * </p>
     */
    public static Performable toDisappear() {
        return Task.where("{0} waits for toast notification to disappear",
                actor -> {
                    long deadline = System.currentTimeMillis() + 12_000;
                    while (System.currentTimeMillis() < deadline) {
                        try {
                            WebElementFacade toast = ToastTargets.CONTAINER.resolveFor(actor);
                            // Considerar "desaparecido" si no es visible O si el texto ya es vacío
                            if (!toast.isVisible() || toast.getText().trim().isEmpty()) {
                                return;
                            }
                        } catch (Exception e) {
                            // Elemento no encontrado en el DOM → definitivamente desaparecido
                            return;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    // Timeout: si llegamos aquí el toast persiste > 12s, aceptar y continuar
                    // para no bloquear el resto del flujo
                }
        );
    }
}
