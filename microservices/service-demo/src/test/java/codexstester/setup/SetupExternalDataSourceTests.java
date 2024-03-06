package codexstester.setup;

import codexstester.abstractor.external.ExternalRequest1XxTestsTests;
import com.huntercodexs.archdemo.demo.AddressApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AddressApplication.class) /*INSERT HERE THEM MAIN CLASS FROM PROJECT (EXAMPLE: ApplicationName.class)*/
public class SetupExternalDataSourceTests extends ExternalRequest1XxTestsTests {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

}