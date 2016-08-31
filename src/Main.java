import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMError;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement;
import com.gargoylesoftware.htmlunit.javascript.host.media.SourceBuffer;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import java.util.*;
import java.util.logging.Level;

/**
 * Created by Snoob on 4/10/2016.
 */
public class Main {
    public static long time = System.currentTimeMillis();

    public static void main(String[] args) throws Exception {


        FileEntryWriter resultFile = new FileEntryWriter("result.txt");
//        Thread accountPoster = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    AccountManager manager = new AccountManager(resultFile);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        accountPoster.start();
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        ProxyList pL = new ProxyList();
        pL.getProxyList();
        UserNameList unl = new UserNameList();
        Thread mThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            unl.getUserList(975084);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        mThread.start();
        //Wait to get username list
        Thread.sleep(1000);
        System.out.println("Get User name");
        System.out.println("Get Proxy");
        for (int i = 0; i < 5; i++) {
            Thread th = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TryPass tp = new TryPass(resultFile,ProxyList.proxyList, UserNameList.userlist);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
            th.start();
        }
        System.out.println("It takes " + (System.currentTimeMillis() - time) / 1000 + "seconds");
    }
}