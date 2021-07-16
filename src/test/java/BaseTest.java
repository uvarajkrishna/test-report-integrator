import com.aventstack.extentreports.service.ExtentService;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.annotations.AfterMethod;

public class BaseTest {

    private static final String EXECUTION_TIME_STAMP =
        new SimpleDateFormat("MMM-dd-yyyy_hh-mm-ss")
            .format(new Date());
    private static final String EXTENT_REPORT_PATH = MessageFormat
        .format("target/test-results/extent/extent-report_{0}.html", EXECUTION_TIME_STAMP);

    static {
        System.setProperty("extent.reporter.html.start", "true");
        System.setProperty("extent.reporter.html.config", "src/test/resources/html-config.xml");
        System.setProperty("extent.reporter.html.out", EXTENT_REPORT_PATH);
    }

    @AfterMethod
    public void tearDown() {
        ExtentService.getInstance().flush();
    }
}
