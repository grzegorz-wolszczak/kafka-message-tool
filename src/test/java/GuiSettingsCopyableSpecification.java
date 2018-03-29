import application.persistence.GuiSettings;
import autofixture.publicinterface.Any;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GuiSettingsCopyableSpecification {

    @Test
    public void shouldCopyAllFieldsFromOneGuiSettingsObjectToAnother() {
        // GIVEN
        GuiSettings a = Any.anonymous(GuiSettings.class);

        GuiSettings toFill = new GuiSettings();

        // WHEN
        toFill.fillFrom(a);

        // THEN
        assertThat(toFill).isEqualToComparingFieldByFieldRecursively(a);
    }

    @Test
    public void shouldIgnoreNullSettingsWhileFilling() {
        // GIVEN
        GuiSettings toFill = new GuiSettings();

        // WHEN/THEN
        toFill.fillFrom(null);
    }
}
