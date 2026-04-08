package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.mileage.MileageData;
import com.fleetguard.e2e.screenplay.mileage.UpdateMileage;
import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.rules.CreateMaintenanceRule;
import com.fleetguard.e2e.screenplay.rules.MaintenanceRuleType;
import com.fleetguard.e2e.screenplay.rules.RuleData;
import com.fleetguard.e2e.screenplay.services.QueryAlerts;
import com.fleetguard.e2e.screenplay.services.RegisterService;
import com.fleetguard.e2e.screenplay.services.ServiceData;
import com.fleetguard.e2e.screenplay.services.ServicesForm;
import com.fleetguard.e2e.screenplay.utils.TestDataGenerator;
import com.fleetguard.e2e.screenplay.utils.WaitForToast;
import com.fleetguard.e2e.screenplay.vehicle.RegisterVehicle;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleType;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.annotations.CastMember;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static org.hamcrest.Matchers.is;

/**
 * Test de ciclo completo — flujo de negocio de FleetGuard en orden correcto.
 *
 * <p><b>Orden del flujo:</b>
 * <ol>
 *   <li>PASO 1: Registrar vehículo (/register)</li>
 *   <li>PASO 2: Crear regla de mantenimiento (/rules) usando tipo VÁLIDO de mockMaintenanceRules</li>
 *   <li>PASO 3: Actualizar kilometraje (/mileage) — supera el intervalo de la regla para disparar alerta</li>
 *   <li>PASO 4: Esperar procesamiento asíncrono RabbitMQ</li>
 *   <li>PASO 5: Consultar alertas en /services</li>
 *   <li>PASO 6: Registrar servicio para resolver la alerta</li>
 * </ol>
 * </p>
 *
 * <p><b>Prerrequisito:</b> docker-compose up (fleet-service + rules-alerts-service + RabbitMQ).</p>
 *
 * <p><b>Regla elegida:</b> {@link MaintenanceRuleType#ACEITE_MOTOR_LIVIANO} — intervalKm=5000.
 * Se registran 6000 km para superar el umbral y disparar la alerta.</p>
 */
@ExtendWith(SerenityJUnit5Extension.class)
class FullFlowTest {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldCompleteFullMaintenanceLifecycle() throws InterruptedException {

        // ── PASO 1: Registrar el vehículo ─────────────────────────────────
        // Debe existir ANTES de crear la regla.
        // VehicleType.SEDAN → "Sedán" → UUID en BD y botón en /rules
        VehicleData vehicle = TestDataGenerator.uniqueVehicleData(VehicleType.SEDAN);
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(vehicle));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // ── PASO 2: Crear regla de mantenimiento para tipo Sedán ──────────
        // ACEITE_MOTOR_LIVIANO → intervalKm=5000, warningThresholdKm=500
        // Registrar 6000 km en el paso 3 superará este intervalo → dispara alerta
        RuleData rule = TestDataGenerator.ruleFor(
                MaintenanceRuleType.ACEITE_MOTOR_LIVIANO,
                VehicleType.SEDAN
        );
        hans.attemptsTo(NavigateTo.theRulesPage());
        hans.attemptsTo(CreateMaintenanceRule.with(rule));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // ── PASO 3: Actualizar kilometraje ────────────────────────────────
        // Lógica de alerta: dueAtKm = ceil(mileage/intervalKm)*intervalKm
        // Para PENDING: registro < dueAtKm pero dentro del warningThreshold
        //   intervalKm=5000, warningThresholdKm=500
        //   Registrar 4750 km → dueAtKm=5000, kmRemaining=250 → 250 ≤ 500 → PENDING ✓
        // EVITAR registrar > intervalKm (ej: 6000) → dueAtKm pasa a 10000, kmRemaining=4000 → SIN ALERTA
        long tripKm = (long)(rule.intervalKm() - rule.warningThresholdKm() / 2); // 5000 - 250 = 4750
        MileageData mileageData = TestDataGenerator.mileageFor(vehicle.plate(), tripKm);
        hans.attemptsTo(NavigateTo.theMileagePage());
        hans.attemptsTo(UpdateMileage.with(mileageData));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // ── PASO 4: Esperar procesamiento asíncrono RabbitMQ ──────────────
        // El rules-alerts-service consume el evento y crea la alerta en su BD.
        // 5 segundos para asegurar procesamiento en entornos con carga.
        Thread.sleep(5000);

        // ── PASO 5: Consultar alertas del vehículo ────────────────────────
        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate(vehicle.plate()));

        hans.attemptsTo(
                WaitUntil.the(ServicesForm.FIRST_ALERT, isVisible()).forNoMoreThan(15).seconds()
        );
        hans.should(seeThat(
                actor -> ServicesForm.FIRST_ALERT.resolveFor(actor).isVisible(), is(true)
        ));

        // ── PASO 6: Registrar el servicio para resolver la alerta ─────────
        ServiceData serviceData = new ServiceData(
                vehicle.plate(),
                "Técnico E2E Full Flow",
                TestDataGenerator.today(),
                "Taller FleetGuard E2E",
                "200.00",
                "Cambio de aceite motor liviano E2E – ciclo completo",
                String.valueOf(tripKm)
        );
        hans.attemptsTo(RegisterService.with(serviceData));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }
}
