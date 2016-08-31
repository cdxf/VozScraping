import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.github.lalyos.jfiglet.FigletFont;

import java.util.Random;

/**
 * Created by Snoob on 4/12/2016.
 */
public class GiveAwayBot {
    public static String randomPassword(){
        Random random = new Random();
        String pass = "";
        Random randomGenerator = new Random();
        for (int idx = 1; idx <= 10; ++idx){
            int randomInt = randomGenerator.nextInt(9);
            pass += String.valueOf(randomInt);
        }
        return pass;
    }
    public static void GiveAwayBot(String[] args) throws Exception {
        String art = FigletFont.convertOneLine(randomPassword());
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(500);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        //Login here
        HtmlPage page = webClient.getPage("https://vozforums.com/newthread.php?do=newthread&f=32");
        HtmlInput usernameInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_username']").get(0);
        HtmlInput passwordInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_password']").get(0);
        HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath("//input[@type='submit' and @value='Log in']").get(0);
        usernameInput.setValueAttribute("chandoituky2014");
        passwordInput.setValueAttribute("2");
        HtmlPage result = null;
        Page mpage = null;
        try {
            mpage = submit.click();
            result = (HtmlPage) mpage;
        } catch (ClassCastException e) {
            //Something wrong here
        }
        HtmlInput title = result.getElementByName("subject");
        title.setValueAttribute("Cho mình thử tí");
        HtmlTextArea contentTA = (HtmlTextArea) result.getByXPath("//textarea").get(0);
        String content = "[CODE]" + art + "[/CODE]";
        contentTA.setText(content);
        HtmlSubmitInput submitBtn = (HtmlSubmitInput) result.getElementById("vB_Editor_001_save");
        HtmlPage a = submitBtn.click();
        System.out.println(a.asText());
    }
}
