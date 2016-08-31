package App;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Snoob on 4/14/2016.
 */
public class BPL_Bot implements IBot{
    HtmlPage page;
    @Override
    public PostInfo getPosts(String title) throws IOException, ParseException {
        if(title == null) return null;
        PostInfo post = null;
        List<HtmlAnchor> byXPath = (List<HtmlAnchor>) page.getByXPath("//a[@title and @title != \"\"]");
        for(HtmlAnchor e : byXPath){
            String compareTitle = Utility.beautify(e.getAttribute("title"));
            if(!compareTitle.equals(title) ){
                continue;
            }
            String url = e.getAttribute("href");
            //Relative URL
            if(!url.contains("http")) url = "http://baophapluat.vn" + url;
            post = new PostInfo(title,url);
            PostDetail pD = this.getPostDetails(url);
            if(pD == null) return null;
            post.details = pD;
        }
        return post;
    }

    private PostDetail getPostDetails(String postUrl) throws ParseException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(500);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        HtmlPage page = null;
        try{
            page = webClient.getPage(postUrl);
        }catch (ClassCastException e){
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String Author = null;
        try {
            HtmlDivision authorDiv = (HtmlDivision) page.getByXPath("//div[@class='name-author']").get(0);
            Author = Utility.beautify(authorDiv.getTextContent());
        }catch (IndexOutOfBoundsException e){
            Author = "";
        }
        List<String> contents;
        HtmlDivision articleContent = (HtmlDivision) page.getElementById("article-content");
        if(articleContent == null){
            System.out.println(postUrl);
        }
        contents = getContent(articleContent);
        if(contents == null) return null;
        HtmlSpan dateSpan = (HtmlSpan) page.getByXPath("//header[@class=\"head-post\"]/span").get(0);
        String dateString = dateSpan.getTextContent();
        dateString = dateString.split(" ")[2];
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy");
        Date date = df.parse(dateString);
        PostDetail pD = new PostDetail();
        List<ContentImg> contentImgs = getContentImage(page,contents);
        pD.Author = Author;
        pD.contents = contents;
        pD.date = date;
        Date now = new Date();
        long duriable = now.getTime() - date.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(duriable);
        if(days >= 2) return null;
        pD.contentImgs = contentImgs;
        return pD;
    }

    private List<String> getContent(HtmlElement articleContent) {
        if(articleContent == null) return null;
        List<String> contents = new ArrayList<>();
        HtmlDivision quoteDiv;
        try{
            List<?> byXPath = articleContent.getByXPath("//div[@class=\"quote\"]/div");
            if(byXPath.size() == 0)
                byXPath = articleContent.getByXPath("//div[@class=\"quote\"]");
            quoteDiv = (HtmlDivision) byXPath.get(0);
            String quote = quoteDiv.getTextContent();
            contents.add(quote);
        }catch (IndexOutOfBoundsException e){
            System.out.println("OOB");
        }
        String content = "";
        HtmlElement innerPost = articleContent.getFirstByXPath(".//div[@class=\"inner-post\"]");
        List<HtmlElement> contentParagraph = (List<HtmlElement>) innerPost.getByXPath(".//*[(self::p or self::div)]");
        for(HtmlElement subContent :contentParagraph){
            if(subContent.getAttribute("class").equals("article-avatar-desc")) continue;
            if(Utility.getDeep(subContent) >1) continue;
            if(subContent.getParentNode().getNodeName().equals("tr") || subContent.getParentNode().getNodeName().equals("td")) continue;
            String mString = Utility.beautify(subContent.getTextContent());
//            System.out.println(mString);
//            System.out.println("Childs " +subContent.getChildElementCount());
            if(subContent.getElementsByTagName("strong").size()>0){
                mString = "[B]" + mString + "[/B]\n";
            }
            if(subContent.getElementsByTagName("em").size()>0){
                mString = "[I]" + mString + "[/I]";
            }
            contents.add(mString);
        }
        return contents;
    }

    private List<ContentImg> getContentImage(HtmlPage page, List<String> contents) {
        List<ContentImg> contentImgs = new ArrayList<>();
        //Get contentImgs
        List<DomNode> imgs = (List<DomNode>) page.getByXPath("//div[@class=\"inner-post\"]/img");
        imgs.addAll((List<DomNode>) page.getByXPath("//*[@class=\"contentimg\"]//img"));
        for(DomNode img: imgs){
            String url = ((HtmlImage)img).getSrcAttribute();
            String description = Utility.getDeepestText(img,3);
            for(String content : contents){
                if(Utility.stringCompare(description,content)>0.8){
                    description = "";
                    break;
                }
            }
            if(description == "" || description == null) description = ((HtmlImage)img).getAltAttribute();
            description = Utility.beautify(description);
            ContentImg ci = new ContentImg(url,description);
            contentImgs.add(ci);
        }

        return contentImgs;
    }

    @Override
    public Set<String> getTitle() throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(500);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        page = webClient.getPage("http://baophapluat.vn/");
        List<HtmlAnchor> byXPath = (List<HtmlAnchor>) page.getByXPath("//a[@title and @title != \"\"]");
        Set<String> titleSet = new HashSet<>();
        for(HtmlAnchor e : byXPath){
            String title = Utility.beautify(e.getAttribute("title"));
            if(title.split(" ").length < 6) continue;
            titleSet.add(title);
        }
        return titleSet;
    }

    @Override
    public String getColor() {
        return "#9A0909";
    }

    @Override
    public String getLogoURL() {
        return "http://gagiongdabaco.com.vn/images/2015/06/17/logo.png";
    }
}
