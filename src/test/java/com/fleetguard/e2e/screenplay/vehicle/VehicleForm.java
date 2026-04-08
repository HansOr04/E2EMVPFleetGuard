package com.fleetguard.e2e.screenplay.vehicle;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Targets de /register — todos los inputs tienen atributo name.
 * Fuente: SELECTORS.md sección 2.
 */
public class VehicleForm {

    public static final Target PAGE_TITLE = Target.the("Título Registrar Vehículo")
            .locatedBy("//h2[normalize-space()='Registrar Nuevo Vehículo']");

    public static final Target PLATE_INPUT = Target.the("Input Placa")
            .locatedBy("//input[@name='plate']");

    public static final Target VIN_INPUT = Target.the("Input VIN")
            .locatedBy("//input[@name='vin']");

    public static final Target BRAND_INPUT = Target.the("Input Marca")
            .locatedBy("//input[@name='brand']");

    public static final Target MODEL_INPUT = Target.the("Input Modelo")
            .locatedBy("//input[@name='model']");

    public static final Target YEAR_SELECT = Target.the("Select Año")
            .locatedBy("//select[@name='year']");

    public static final Target FUEL_TYPE_SELECT = Target.the("Select Tipo Combustible")
            .locatedBy("//select[@name='fuelType']");

    public static final Target VEHICLE_TYPE_SELECT = Target.the("Select Tipo de Vehículo")
            .locatedBy("//select[@name='vehicleTypeId']");

    public static final Target SUBMIT_BUTTON = Target.the("Botón Guardar Vehículo")
            .locatedBy("//button[@type='submit' and contains(.,'Guardar Vehículo')]");
}
