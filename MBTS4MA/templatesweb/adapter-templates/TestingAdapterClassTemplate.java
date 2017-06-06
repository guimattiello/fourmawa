package adapter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
{{testingimportpageobjects}}

public class {{testingclassname}}Adapter {

	WebDriver driver;
	{{testingpageobjects}}

    public {{testingclassname}}Adapter(WebDriver driver, {{testingparameterpageobject}}) {
    	this.driver = driver;
        {{testingpageobjectinit}}
    }

{{testingmethodtemplate}}

}
