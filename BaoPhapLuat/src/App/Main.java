package App;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Snoob on 4/12/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException, InterruptedException, ExecutionException {
       //Set<App.TitleStats> titleStatses = App.F33TitleScraper.get(1, 100, 4);
//        App.IBot kk = new App.BPL_Bot();
//        for(String e : kk.getTitle()){
//            App.PostInfo posts = kk.getPosts(e);
//            System.out.println(posts.toBBCODE());
//            break;
//        }
        F33PostBot bot = new F33PostBot();
    }
}
