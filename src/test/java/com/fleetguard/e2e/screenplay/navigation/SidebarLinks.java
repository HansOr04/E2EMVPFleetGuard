package com.fleetguard.e2e.screenplay.navigation;

import net.serenitybdd.screenplay.targets.Target;

public class SidebarLinks {
    public static final Target HOME = Target.the("Link Home")
            .locatedBy("//nav//a[@href='/']");
    public static final Target REGISTER_VEHICLE = Target.the("Link Registrar Vehículo")
            .locatedBy("//nav//a[@href='/register']");
    public static final Target RULES = Target.the("Link Reglas de Mantenimiento")
            .locatedBy("//nav//a[@href='/rules']");
    public static final Target MILEAGE = Target.the("Link Actualizar Kilometraje")
            .locatedBy("//nav//a[@href='/mileage']");
    public static final Target SERVICES = Target.the("Link Registro de Servicios")
            .locatedBy("//nav//a[@href='/services']");
}
