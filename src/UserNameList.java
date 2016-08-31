import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Snoob on 4/11/2016.
 */
public class UserNameList {
    public static Queue<String> userlist = new ArrayDeque<>();
    //This will run forever in a thread
    public void getUserList(int startID) throws Exception{
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(500);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        //Try 20 account this momment
        for (int i = startID; true; i++) {
            if(userlist.size() > 100) Thread.sleep(120000);
            HtmlPage page = webClient.getPage("https://vozforums.com/member.php?u=" + i);
            String pageTitle =  page.getTitleText();
            try {
                String username = pageTitle.substring("vozForums - View Profile: ".length());
                userlist.add(username);
            }catch (StringIndexOutOfBoundsException e){
                continue;
            }
        }
    }
}
