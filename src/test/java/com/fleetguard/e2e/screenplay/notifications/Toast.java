package com.fleetguard.e2e.screenplay.notifications;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

/**
 * Questions para validar el estado del toast de notificación.
 */
public class Toast {

    /** Retorna el texto del mensaje dentro del toast. */
    public static Question<String> message() {
        return actor -> Text.of(ToastTargets.MESSAGE).answeredBy(actor);
    }

    /** Retorna true si el container del toast está visible en el DOM. */
    public static Question<Boolean> isVisible() {
        return actor -> {
            try {
                return ToastTargets.CONTAINER.resolveFor(actor).isVisible();
            } catch (Exception e) {
                return false;
            }
        };
    }

    /** Retorna true si el toast muestra icono de éxito (check_circle). */
    public static Question<Boolean> isSuccessToast() {
        return actor -> {
            try {
                return ToastTargets.SUCCESS_ICON.resolveFor(actor).isVisible();
            } catch (Exception e) {
                return false;
            }
        };
    }

    /** Retorna true si el toast muestra icono de error (cancel). */
    public static Question<Boolean> isErrorToast() {
        return actor -> {
            try {
                return ToastTargets.ERROR_ICON.resolveFor(actor).isVisible();
            } catch (Exception e) {
                return false;
            }
        };
    }
}
