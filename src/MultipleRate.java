import com.gargoylesoftware.htmlunit.WebClient;

import java.util.Random;

/**
 * Created by Snoob on 7/27/2016.
 */
public class MultipleRate {
    public static class Rate implements Runnable{
        String token;
        WebClient webClient;
        int thread;
        boolean done = false;
        public Rate(String token, WebClient webClient, Integer thread) {
            this.token = token;
            this.webClient = webClient;
            this.thread = thread;
        }
        @Override
        public void run() {
            try {
                ThreadList.onerate(webClient,token,String.valueOf(thread));
                done = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void rate(String username,String password,Integer startThread) throws Exception {
        Integer thread = startThread;
        while(true){
            WebClient webClient = Context.getWebClient();
            String token = ThreadList.login(webClient,username,password);
            for(int r =0;r<100;r++){
                if(ThreadList.rand.nextInt(1000) > 987) {
                    token = ThreadList.getToken(webClient);
                    //System.out.println(token);
                }
                Rate[] rates = new Rate[10];
                for(int i =0;i<10;i++){
                    rates[i] = new Rate(token,webClient,thread + i);
                    Context.threadPool.execute(rates[i]);
                    Thread.sleep(100);
                }
                int k =0;
                while(k!=10){
                    if(rates[k].done) k++;
                    else Thread.sleep(50);
                }
                webClient.close();
                thread+= 10;
                Thread.sleep(150);
                if(thread%500 > 1 && thread%500 <11) System.out.println(thread + " ");
            }
            System.out.println("New Loop");
            webClient.close();
        }
    }
    public static void rate(String username,String password) throws Exception {
        Context.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    rate(username,password,4403015);
                    //4310601
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static void main(String[] args) throws Exception {
        rate("squall124", "123456789");
        rate("Galaxys1", "123456789");
        rate("tiger13", "123456789");
    }
}
