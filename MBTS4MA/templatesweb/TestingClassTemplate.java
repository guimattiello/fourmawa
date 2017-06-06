import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import adapter.{{testingclassname}}Adapter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
{{testingimportpageobjects}}
{{otherimports}}

public class {{testingclassname}}Test {
    
    public WebDriver driver = null;
    
    {{testingpageobjects}}

    {{testingclassname}}Adapter adapter;
    
    public {{testingclassname}}Test() {
        System.setProperty("webdriver.chrome.driver", "/Users/guimat/Desktop/chromedriver");
        driver = new ChromeDriver();
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException, UnsupportedEncodingException {        
        {{calldbclass}}      
    }
    
    @AfterClass
    public static void tearDownClass() {

    }
    
    @Before
    public void setUp() {
        driver.get("{{testingurlinicio}}");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        {{testingsetup}}
        adapter = new {{testingclassname}}Adapter(this.driver, {{testinginstatiateadapter}});
    }
    
    @After
    public void tearDown() {
        driver.close();
    }

{{testingmethodtemplate}}
}