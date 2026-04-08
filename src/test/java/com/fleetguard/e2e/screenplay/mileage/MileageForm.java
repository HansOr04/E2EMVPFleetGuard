package com.fleetguard.e2e.screenplay.mileage;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Targets de /mileage — los inputs NO tienen atributo name.
 * Se usa patrón XPath: label + following-sibling::div//input.
 * Fuente: SELECTORS.md sección 3.
 * ADVERTENCIA: el input de km es type=number; NO usar scroll del mouse.
 */
public class MileageForm {

    public static final Target PAGE_TITLE = Target.the("Título Actualizar Kilometraje")
            .locatedBy("//h2[normalize-space()='Actualizar Kilometraje']");

    public static final Target PLATE_INPUT = Target.the("Input Placa (Kilometraje)")
            .locatedBy("//label[contains(normalize-space(),'Placa del Vehículo')]/following-sibling::div//input");

    public static final Target MILEAGE_INPUT = Target.the("Input Nuevo Kilometraje")
            .locatedBy("//label[contains(normalize-space(),'Nuevo Kilometraje')]/following-sibling::div//input");

    public static final Target RECORDED_BY_INPUT = Target.the("Input Registrado por (Kilometraje)")
            .locatedBy("//label[contains(normalize-space(),'Registrado por')]/following-sibling::div//input");

    public static final Target SUBMIT_BUTTON = Target.the("Botón Actualizar Odómetro")
            .locatedBy("//button[@type='submit' and contains(.,'Actualizar Odómetro')]");
}
