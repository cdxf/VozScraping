import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Snoob on 4/11/2016.
 */
public class ProxyList {
    public static Queue<ProxyInfo> proxyList = new ArrayDeque<>();
    public void getProxyList() throws Exception{
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(500);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        for (int i = 1; i<=20; i++) {
            HtmlPage page = webClient.getPage("http://www.top-proxies.co.uk/index.php?p=main&page=" + i);
            List<?> xPath = page.getByXPath("//td/a[@target=\"_blank\"]");
            for (HtmlAnchor e: (List<HtmlAnchor>) xPath) {
                ProxyInfo proxy = new ProxyInfo();
                if(!e.getTextContent().contains("http")) continue;
                proxy.ProxyURL = e.getTextContent();
                if(!proxyList.contains(proxy))
                    proxyList.add(proxy);
            }
        }
        for (int i = 1; i<=8; i++) {
            HtmlPage page = webClient.getPage("http://www.gatherproxy.com/webproxylist/?p=" + i);
            List<?> xPath = page.getByXPath("//td[2]/a");
            for (HtmlElement e: (List<HtmlElement>) xPath) {
                ProxyInfo proxy = new ProxyInfo();
                proxy.ProxyURL = e.getAttribute("href");
                if(!proxyList.contains(proxy))
                    proxyList.add(proxy);
            }
        }
        for (int i = 1; i<=6; i++) {
            HtmlPage page = webClient.getPage("http://www.proxy4free.com/list/webproxy" + i + ".html");
            List<?> xPath = page.getByXPath("//td[2]/a");
            for (HtmlElement e: (List<HtmlElement>) xPath) {
                ProxyInfo proxy = new ProxyInfo();
                proxy.ProxyURL = e.getAttribute("href");
                if(!proxyList.contains(proxy))
                    proxyList.add(proxy);
            }
        }
        System.out.println("Get total "+ proxyList.size() + "proxy");
        webClient.close();
    }
    public synchronized static ProxyInfo getBestProxy(Queue<ProxyInfo> proxyList){
        ProxyInfo bestProxy = null;
        long shorstest = System.currentTimeMillis();
        for(ProxyInfo p: proxyList){
            if(p.expire< shorstest) {
                shorstest = p.expire;
                bestProxy = p;
            }
        }
        proxyList.remove(bestProxy);
        return bestProxy;
    }
}
