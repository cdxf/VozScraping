import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Snoob on 4/12/2016.
 */
public class RandomTitle {
    public static String get() throws IOException {
        String title = null;
        while(title == null) {
            Random randomGenerator = new Random();
            int randomPage = randomGenerator.nextInt(2000) + 2;
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.setJavaScriptTimeout(500);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setUseInsecureSSL(true);
            //Login here
            HtmlPage page = webClient.getPage("https://vozforums.com/forumdisplay.php?f=17&order=desc&page=" + String.valueOf(randomPage));
            List<HtmlAnchor> thread = (List<HtmlAnchor>) page.getByXPath("//a[starts-with(@id,\"thread_title\")]");
            int randomThread = randomGenerator.nextInt(thread.size()-1);
            title = thread.get(randomThread).getTextContent();
            webClient.close();
        }
        return title;
    }
}
