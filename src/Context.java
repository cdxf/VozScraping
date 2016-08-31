import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Snoob on 7/28/2016.
 */
public class Context {
    public static ExecutorService threadPool = Executors.newCachedThreadPool();
    public static HtmlPage getPage(WebClient webClient, String url) throws InterruptedException{
        HtmlPage page = null;
        while(page == null) {
            try {
                page = webClient.getPage(url);
            } catch (Exception e){
                webClient.close();
                System.out.println("Stuck at" + url);
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }
        return page;
    }
    public static HtmlPage getPage(WebClient webClient, WebRequest url) throws InterruptedException {
        HtmlPage page = null;
        while(page == null) {
            try {
                page = webClient.getPage(url);
            } catch (Exception e) {
                webClient.close();
                System.out.println("Stuck at" + url.getUrl());
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }
        return page;
    }
    public static WebClient getWebClient(){
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(500);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        return webClient;
    }
}
