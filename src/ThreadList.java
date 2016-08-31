import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Snoob on 4/11/2016.
 */
public class ThreadList {
    public static Vector<HashSet<String>> threadListArray = new Vector<>();
    public static Random rand = new Random();
    //This will run forever in a thread
    public static void threadList(String forums,int startPage,boolean forward) throws Exception{
        System.out.println("Start threadList");
        WebClient webClient = Context.getWebClient();
        HtmlPage page = webClient.getPage("https://vozforums.com");
        HtmlInput usernameInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_username']").get(0);
        HtmlInput passwordInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_password']").get(0);
        HtmlCheckBoxInput rememberMe = (HtmlCheckBoxInput)   page.getElementById("cb_cookieuser_navbar");
        rememberMe.setChecked(true);
        HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath("//input[@type='submit' and @value='Log in']").get(0);
        usernameInput.setValueAttribute("testmuasan01");
        passwordInput.setValueAttribute("123456789");
        HtmlPage result = null;
        Page mpage = null;
        try {
            mpage = submit.click();
            result = (HtmlPage) mpage;
        } catch (ClassCastException e) {
            //Something wrong here
        }
        //Login done
        Integer pagenumber = startPage;
        while(true) {
            page = webClient.getPage("https://vozforums.com/forumdisplay.php?f=" + forums + "&page=" + pagenumber);
            List<HtmlTableDataCell> byXPath = (List<HtmlTableDataCell>) page.getByXPath("//*[contains(@id,\"td_threadtitle\")]");
            for (HtmlTableDataCell e : byXPath) {
                if (e.getByXPath(".//img[contains(@alt,\"Thread Rating\")]").size() >= 1) continue;
                if (e.getByXPath(".//img[contains(@alt,\"Sticky Thread\")]").size() >= 1) continue;
                HtmlImage threadType = e.getFirstByXPath("./preceding-sibling::td[1]//img");
                if(threadType.getAttribute("src").contains("lock")) continue;
                String thread = e.getId().substring("td_threadtitle_".length());
                if(thread.length()>10) continue;
                synchronized (threadListArray) {
                    for(HashSet<String> threadList : threadListArray){
                        threadList.add(thread);
                    }
                }
            }
            if(forward) {
                pagenumber++;
                Thread.sleep(5000);
            }
            else{
                Thread.sleep(2000);
            }
            webClient.close();
        }
    }
    public static String login(WebClient webClient, String username,String password) throws IOException {
        HtmlPage page = webClient.getPage("https://vozforums.com");
        HtmlInput usernameInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_username']").get(0);
        HtmlInput passwordInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_password']").get(0);
        HtmlCheckBoxInput rememberMe = (HtmlCheckBoxInput)   page.getElementById("cb_cookieuser_navbar");
        rememberMe.setChecked(true);
        HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath("//input[@type='submit' and @value='Log in']").get(0);
        usernameInput.setValueAttribute(username);
        passwordInput.setValueAttribute(password);
        HtmlPage result = null;
        Page mpage = null;
        try {
            mpage = submit.click();
            result = (HtmlPage) mpage;
        } catch (ClassCastException e) {
            //Something wrong here
        }
        String token = result.getElementByName("securitytoken").getAttribute("value");
        webClient.close();
        return token;
    }
    public static void onerate(WebClient webClient, String token,String thread) throws Exception{
        Page result = null;
        URL url = new URL("https://vozforums.com/threadrate.php?t=" + thread);
        WebRequest requestSettings = new WebRequest(url, HttpMethod.POST);
        int vote = rand.nextInt(5) + 1;
        requestSettings.setRequestBody("vote=" + vote + "&securitytoken=" + token + "&t=" + thread);
        while(result == null) {
            try {
                result = Context.getPage(webClient, requestSettings);
            } catch (NullPointerException e) {
                System.out.println("NULLPOINT");
                result = null;
                Thread.sleep(1000);
            } catch (FailingHttpStatusCodeException e) {
                System.out.println("HttpStatusFail");
                result = null;
                Thread.sleep(1000);
            } catch (InterruptedException e){
                System.out.println("Interrupted");
            }
        }
    }
    public static String getToken(WebClient webClient) throws  Exception{
        String token = null;
        while(token == null) {
            HtmlPage page = Context.getPage(webClient, "https://vozforums.com");
            try{
                token = page.getElementByName("securitytoken").getAttribute("value");
            }catch (ElementNotFoundException e){
                System.out.println("Token stuck");
            }
        }
        webClient.close();
        return token;
    }
    public static void rate(String username,String password) throws Exception{
        HashSet<String> list = new HashSet<>();
        threadListArray.add(list);
        WebClient webClient = Context.getWebClient();
        String token = login(webClient,username,password);
        int i = 0;
       while(true){
           if(rand.nextInt(1000) > 985) {
               token = getToken(webClient);
               System.out.println(token);
           }
           if(list.size()>0){
               String thread = null;
               synchronized (threadListArray){
                   thread = list.iterator().next();
                   list.remove(thread);
               }
               onerate(webClient,token,thread);
               //System.out.println("Thread " + thread);
               webClient.close();
               i++;
           }else{
               Thread.sleep(5000);
           }
        }
    }
}
