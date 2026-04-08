package com.fleetguard.e2e.screenplay.services;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Targets de /services — flujo en 2 pasos.
 * Los targets del Paso 2 son CONDICIONALES: solo aparecen en el DOM
 * después de que "Consultar alertas" devuelve resultados.
 * Fuente: SELECTORS.md sección 5.
 */
public class ServicesForm {

    // --- Paso 1: Identificación ---
    public static final Target PAGE_TITLE = Target.the("Título Registro de Servicios")
            .locatedBy("//h2[normalize-space()='Registro de Servicios']");

    public static final Target PLATE_INPUT = Target.the("Input Placa (Servicios Paso 1)")
            .locatedBy("//label[contains(normalize-space(),'Placa')]/following-sibling::div//input");

    public static final Target QUERY_ALERTS_BUTTON = Target.the("Botón Consultar alertas")
            .locatedBy("//button[contains(.,'Consultar alertas')]");

    public static final Target NO_ALERTS_MESSAGE = Target.the("Mensaje No hay alertas activas")
            .locatedBy("//*[contains(normalize-space(),'No hay alertas activas para este')]");

    // --- Paso 2: Registro del servicio (CONDICIONAL) ---
    public static final Target FIRST_ALERT = Target.the("Primera alerta disponible")
            .locatedBy("(//div[contains(.,'Vence a los')])[1]");

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

    public static final Target SUBMIT_BUTTON = Target.the("Botón Registrar Servicio")
            .locatedBy("//button[@type='submit' and contains(.,'Registrar Servicio')]");
}
