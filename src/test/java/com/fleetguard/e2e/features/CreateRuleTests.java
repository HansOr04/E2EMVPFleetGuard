package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.rules.CreateMaintenanceRule;
import com.fleetguard.e2e.screenplay.rules.MaintenanceRuleType;
import com.fleetguard.e2e.screenplay.rules.RuleData;
import com.fleetguard.e2e.screenplay.rules.RulesForm;
import com.fleetguard.e2e.screenplay.utils.TestDataGenerator;
import com.fleetguard.e2e.screenplay.utils.WaitForToast;
import com.fleetguard.e2e.screenplay.vehicle.VehicleType;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.annotations.CastMember;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static org.hamcrest.Matchers.is;

/**
 * Tests para /rules — Crear Regla de Mantenimiento.
 * Orden en el flujo completo: PASO 2 (después de registrar vehículo).
 *
 * <p>El "Nombre de la Regla" es un buscador de tipos predefinidos en mockMaintenanceRules.
 * SIEMPRE usar {@link MaintenanceRuleType} — never free text.</p>
 */
@ExtendWith(SerenityJUnit5Extension.class)
class CreateRuleTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldCreateMaintenanceRuleSuccessfully() {
        // Aceite motor liviano para Sedán y SUV — ambos tipos válidos en vehicle_type
        RuleData ruleData = TestDataGenerator.ruleFor(
                MaintenanceRuleType.ACEITE_MOTOR_LIVIANO,
                VehicleType.SEDAN, VehicleType.SUV
        );

        hans.attemptsTo(NavigateTo.theRulesPage());
        hans.attemptsTo(CreateMaintenanceRule.with(ruleData));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }

    @Test
    void shouldShowInlineMessageWhenNoRuleNameMatches() {
        // Escribir en el buscador sin seleccionar ninguna sugerencia
        // → el sistema muestra "No se encontraron reglas para esta búsqueda"
        // → NO aparece toast (es validación frontend inline)
        hans.attemptsTo(NavigateTo.theRulesPage());
        hans.attemptsTo(Click.on(RulesForm.NAME_INPUT));
        // Escribir texto que no existe en mockMaintenanceRules
        hans.attemptsTo(
                net.serenitybdd.screenplay.actions.Enter.theValue("Tipo Inexistente XYZ")
                        .into(RulesForm.NAME_INPUT)
        );

        // Verificar que aparece el mensaje inline "No se encontraron reglas"
        hans.attemptsTo(
                WaitUntil.the(RulesForm.NO_MATCH_MESSAGE, isVisible()).forNoMoreThan(5).seconds()
        );
        hans.should(seeThat(
                actor -> RulesForm.NO_MATCH_MESSAGE.resolveFor(actor).isVisible(), is(true)
        ));
    }
}
