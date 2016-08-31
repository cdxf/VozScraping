package App;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by Snoob on 4/13/2016.
 */
public class F33TitleScraper {
    private F33TitleScraper() {
    }
    public static Set<TitleStats> get(int start,int end, int thread,int forum) throws IOException, ExecutionException, InterruptedException {
        Set<TitleStats> result = new HashSet<>();
        ExecutorService executorService = Executors.newFixedThreadPool(thread);
        int numPerThread = (end-start+1)/thread;
        System.out.println(numPerThread);
        ArrayList<Future<Set<TitleStats>>> list = new ArrayList<>();
        for(int i = 0;i<thread;i++){
            CustomCallable c = new CustomCallable(i,start,end,thread,forum);
            list.add(executorService.submit(c));
        }
        for(int i = 0;i<thread;i++){
            result.addAll(list.get(i).get());
        }
        return result;
    }
    public static void getTest(int start,int end){
        for (int i = start; i <= end; i++) {
            System.out.println(i);
        }
    }
    public static Set<TitleStats> get(int start,int end,int forum) throws IOException {
        Set<TitleStats> result = new HashSet<>();
        for (int i = start; i <= end; i++) {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.setJavaScriptTimeout(500);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setUseInsecureSSL(true);
            HtmlPage page = webClient.getPage("https://vozforums.com/forumdisplay.php?f=" + forum + "&order=desc&page=" + (i + 1));
            List<HtmlTableRow> tableBody = (List<HtmlTableRow>) page.getByXPath("//tbody[contains(@id,\"threadbits_forum_\")]/tr");
            for (HtmlTableRow e : tableBody) {
                HtmlElement htmlThread = e.getFirstByXPath(".//a[starts-with(@id,\"thread_title\")]");
                HtmlElement repliesE = e.getFirstByXPath("./td[4]/a");
                HtmlElement viewsE = e.getFirstByXPath("./td[5]");
                if (htmlThread == null || repliesE == null || viewsE == null) continue;
                String title = htmlThread.getTextContent();
                int replies = Integer.valueOf(repliesE.getTextContent().replaceAll(",",""));
                int views = Integer.valueOf(viewsE.getTextContent().replaceAll(",",""));
                TitleStats ts = new TitleStats(title, views, replies);
                result.add(ts);
            }
            webClient.close();
        }
        return result;
    }
    public static Set<TitleStats> get(int nPages,int forum) throws IOException {
        return get(0,nPages,forum);
    }
}