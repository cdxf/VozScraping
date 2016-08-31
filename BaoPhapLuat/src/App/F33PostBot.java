package App;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by Snoob on 4/13/2016.
 */
public class F33PostBot {
    WebClient webClient;
    Set<String> f33Thread;
    Set<PostInfo> posts;

    public void webClientInit() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(500);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
    }

    public boolean postThread(int forums, String subject, String content) throws IOException {
        HtmlPage page = webClient.getPage("https://vozforums.com/newthread.php?do=newthread&f=" + forums);
        HtmlInput subjectInput = (HtmlInput) page.getByXPath("//input[@name=\"subject\"]").get(0);
        HtmlTextArea vB_editor_001_textarea = (HtmlTextArea) page.getElementById("vB_Editor_001_textarea");
        HtmlSubmitInput vB_Editor_001_save = (HtmlSubmitInput) page.getElementById("vB_Editor_001_save");
        subjectInput.setValueAttribute(subject);
        vB_editor_001_textarea.setText(content);
        HtmlPage click = vB_Editor_001_save.click();
        String result = click.asText();
        if (result.contains("The following errors occurred with your submission") && result.contains("Okay"))
            return true;
        else {
            System.out.println(click.asText());
            return false;
        }

    }

    public boolean login(String user, String password) throws IOException {
        HtmlPage page = webClient.getPage("https://vozforums.com");
        HtmlInput vb_login_username = page.getElementByName("vb_login_username");
        HtmlInput vb_login_password = page.getElementByName("vb_login_password");
        HtmlSubmitInput submitBtn = (HtmlSubmitInput) page.getByXPath("//input[@value=\"Log in\"]").get(0);
        vb_login_username.setValueAttribute(user);
        vb_login_password.setValueAttribute(password);
        page = submitBtn.click();
        if (page.asText().contains("You last visited")) return true;
        else {
            System.out.println(page.asText());
        }
        return false;
    }

    public String choiceBestPost(snoob.Vocabulary vocabulary, Set<String> choiceTitle, Set<TitleStats> samples, Set<String> ignoreTitle) {
        double highestScore = 0;
        String bestChoice = null;
        for (String title : choiceTitle) {
            boolean isSimilar = false;
            double score = 0;
            if(ignoreTitle.contains(title)) continue;
            long count = 0;
            long time = System.currentTimeMillis();
            for (TitleStats sample : samples) {
                double similar = vocabulary.score(title, sample.title);
                count++;
                score +=  similar * sample.score;
                if(count>50000 || (System.currentTimeMillis() - time)>1000) break;
            }
            if (isSimilar) continue;
            //else caculate score
            if (score > highestScore) {
                highestScore = score;
                bestChoice = title;
            }
        }
        return bestChoice;
    }

    public F33PostBot() throws IOException, ParseException, InterruptedException, ExecutionException, ClassNotFoundException {
        webClientInit();
        System.out.println("Login Result: " + login("snoob7", "123456789"));
        Set<TitleStats> f33posts = new HashSet<>();
        // Larger sample
        SimpleKeyStoring<TitleStats> skS = new SimpleKeyStoring<>("cache.data");
        if(skS.isFirstTime()){
            Set<TitleStats> titleStatses = F33TitleScraper.get(11, 500, 4);
            skS.getList().addAll(titleStatses);
        }
            f33posts.addAll(skS.getList());
            skS.close();
        snoob.Vocabulary v = new snoob.Vocabulary();
        for(TitleStats f33title : f33posts){
            v.put(f33title.title);
        }
        Set<String> ignored = new HashSet<>();
        while (true) {
            Set<TitleStats> titleStats = F33TitleScraper.get(20,33);
            for (TitleStats ts : titleStats) {
                if (f33posts.contains(ts)) {
                    //Update the stats
                    f33posts.remove(ts);
                    f33posts.add(ts);
                }
            }
            f33posts.addAll(titleStats);

            IBot bpl = new BPL_Bot();
            Set<String> titles = bpl.getTitle();
            PostInfo posts = null;
            while(posts == null) {
                long begin = System.currentTimeMillis();
                String bestPost = choiceBestPost(v, titles, f33posts, ignored);
                System.out.println("Best post: " + bestPost);
                System.out.println("Costing: " + (System.currentTimeMillis() - begin));
                if(bestPost == null){
                    Thread.sleep(3600000L);
                }
                posts = bpl.getPosts(bestPost);
                for(TitleStats a :titleStats) {
                    if (Utility.stringCompare(bestPost,a.title)>0.9) {
                        ignored.add(bestPost);
                        posts = null;
                        break;
                    }
                }
                //Similar post, ignore it
                if(posts == null) ignored.add(bestPost);
            }
            System.out.println("Posting " + posts.title);
            postThread(33,posts.title,posts.toBBCODE());
            Thread.sleep(300000*3);
        }
        //f33Thread = App.F33TitleScraper.get(2);
        // posts = BaoPhapLuatScraper.getPosts();
        //If posts already exist in F33, we will ignore it
    }
}
