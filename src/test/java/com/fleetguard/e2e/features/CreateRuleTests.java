package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.rules.CreateMaintenanceRule;
import com.fleetguard.e2e.screenplay.rules.RuleData;
import com.fleetguard.e2e.screenplay.rules.RulesForm;
import com.fleetguard.e2e.screenplay.utils.TestDataGenerator;
import com.fleetguard.e2e.screenplay.utils.WaitForToast;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.annotations.CastMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SerenityJUnit5Extension.class)
class CreateRuleTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldCreateMaintenanceRuleSuccessfully() {
        RuleData ruleData = TestDataGenerator.uniqueRuleData("Sedán", "SUV");

        hans.attemptsTo(NavigateTo.theRulesPage());
        hans.attemptsTo(CreateMaintenanceRule.with(ruleData));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }

    @Test
    void shouldShowValidationErrorWhenRuleNameIsEmpty() {
        hans.attemptsTo(NavigateTo.theRulesPage());
        // Submit sin llenar ningún campo
        hans.attemptsTo(Click.on(RulesForm.SUBMIT_BUTTON));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }
}
