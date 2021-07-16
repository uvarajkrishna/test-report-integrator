import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTest extends BaseTest{

    @Test
    public void testBuild() {
        givenTheStateOfApplication("Not Started");
        whenAnActionIsPerformed("build");
        thenIExpectTheOutcomeToBe("Completed");
    }

    @Test
    public void testDestroy() {
        givenTheStateOfApplication("Not Started");
        whenAnActionIsPerformed("build");
        thenIExpectTheOutcomeToBe("Deleted");
    }

    @Step("Given the State of application - {status}")
    private void givenTheStateOfApplication(String status) {
    }

    @Step("When an action is performed - {action}")
    private void whenAnActionIsPerformed(String action) {

    }

    @Step("Then I expect the outcome to be - {status}")
    private void thenIExpectTheOutcomeToBe(String status) {
        Assert.assertEquals(status,"Completed");
    }
}
