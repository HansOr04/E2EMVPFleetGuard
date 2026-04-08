package com.fleetguard.e2e.screenplay.services;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Targets de /services — flujo en 2 pasos.
 * Fuente: SELECTORS.md sección 5 + DOM inspeccionado en ejecución.
 *
 * <p><b>Estructura real del DOM de alertas (verificado):</b></p>
 * <pre>
 * &lt;button type="button" class="w-full text-left rounded-xl p-4 border-2 ..."&gt;
 *   &lt;div class="flex items-center justify-between gap-4"&gt;
 *     &lt;div class="flex-1"&gt;
 *       &lt;p class="font-bold"&gt;Cambio de aceite motor liviano&lt;/p&gt;
 *       &lt;p class="text-sm"&gt;Vence a los &lt;span&gt;5,000 km&lt;/span&gt;&lt;/p&gt;
 *     &lt;/div&gt;
 *     &lt;span class="..."&gt;Pendiente&lt;/span&gt;
 *   &lt;/div&gt;
 * &lt;/button&gt;
 * </pre>
 * <p>El selector DEBE ser un {@code button[@type='button']} — no un div.</p>
 */
public class ServicesForm {

    private ServicesForm() { /* utility class */ }

    // ── Paso 1: Identificación ─────────────────────────────────────────────

    public static final Target PAGE_TITLE = Target.the("Título Registro de Servicios")
            .locatedBy("//h2[normalize-space()='Registro de Servicios']");

    public static final Target PLATE_INPUT = Target.the("Input Placa (Servicios Paso 1)")
            .locatedBy("//label[contains(normalize-space(),'Placa')]/following-sibling::div//input");

    public static final Target QUERY_ALERTS_BUTTON = Target.the("Botón Consultar alertas")
            .locatedBy("//button[contains(.,'Consultar alertas')]");

    public static final Target NO_ALERTS_MESSAGE = Target.the("Mensaje No hay alertas activas")
            .locatedBy("//*[contains(normalize-space(),'No hay alertas activas para este')]");

    // ── Paso 2: Alertas y registro del servicio (CONDICIONAL) ─────────────

    /**
     * Primera alerta disponible — es un BUTTON[@type='button'], no un div.
     * Verificado en el DOM real: el elemento clickeable es el botón contenedor.
     * XPath: primer button con un párrafo hijo que contiene "Vence a los".
     */
    public static final Target FIRST_ALERT = Target.the("Primera alerta disponible")
            .locatedBy("(//button[@type='button' and .//p[contains(normalize-space(),'Vence a los')]])[1]");

    public static final Target RECORDED_BY_INPUT = Target.the("Input Registrado por (Servicio)")
            .locatedBy("//input[@name='recordedBy']");

    public static final Target PERFORMED_AT_INPUT = Target.the("Input Fecha del Servicio")
            .locatedBy("//input[@name='performedAt']");

    public static final Target PROVIDER_INPUT = Target.the("Input Proveedor (Taller)")
            .locatedBy("//input[@name='provider']");

    public static final Target COST_INPUT = Target.the("Input Costo del Servicio")
            .locatedBy("//input[@name='cost']");

    public static final Target DESCRIPTION_TEXTAREA = Target.the("Textarea Descripción del Trabajo")
            .locatedBy("//textarea[@name='description']");

    public static final Target MILEAGE_AT_SERVICE_INPUT = Target.the("Input Kilometraje al Servicio")
            .locatedBy("//input[@name='mileageAtService']");

    /**
     * Botón submit — tiene atributo {@code disabled} hasta que los campos required estén llenos.
     * Required: recordedBy, performedAt, mileageAtService.
     * Llenar PRIMERO recordedBy y performedAt antes de mileageAtService para que habilitarse.
     */
    public static final Target SUBMIT_BUTTON = Target.the("Botón Registrar Servicio")
            .locatedBy("//button[@type='submit' and contains(.,'Registrar Servicio')]");
}
