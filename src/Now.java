import java.io.IOException;
import java.util.Random;

/**
 * Created by Snoob on 4/12/2016.
 */
public class Now {
    public static void threadStar(String user,String pass){
        Context.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ThreadList.rate(user,pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static void main(String[] args) throws Exception {
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ThreadList.threadList("17",1,false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread mThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ThreadList.threadList("33",1,false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread mThread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ThreadList.threadList("17",1,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread mThread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ThreadList.threadList("33",1,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        threadStar("squall124", "123456789");
        threadStar("Galaxys1", "123456789");
        threadStar("tiger13", "123456789");
        threadStar("testmuasan01", "123456789");
        mThread.start();
        mThread2.start();
        //Foward
    }
}
