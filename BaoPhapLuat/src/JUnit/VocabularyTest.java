package JUnit;

import App.F33TitleScraper;
import App.SimpleKeyStoring;
import App.TitleStats;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by Snoob on 4/14/2016.
 */
public class VocabularyTest {
    String test = " Gần 2.000 công nhân ngừng việc, ném trứng thối vào người đi làm";
    @Test
    public void testTrung(){
    }
    @Test
    public void testVocabulary() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        long time1 = System.currentTimeMillis();
        Set<TitleStats> f33posts = new HashSet<>();
        // Larger sample
        SimpleKeyStoring<TitleStats> skS = new SimpleKeyStoring<>("cache.data");
        if(skS.isFirstTime()){
            Set<TitleStats> titleStatses = F33TitleScraper.get(1, 1000, 16,33);
            skS.getList().addAll(titleStatses);
        }
        SimpleKeyStoring<TitleStats> skS1 = new SimpleKeyStoring<>("cache2.data");
        if(skS1.isFirstTime()){
            Set<TitleStats> titleStatses = F33TitleScraper.get(1, 1000, 16,17);
            skS1.getList().addAll(titleStatses);
        }
        f33posts.addAll(skS.getList());
        System.out.println("Getted + " + skS.getList().size());
        f33posts.addAll(skS1.getList());
        System.out.println("Getted + " + skS1.getList().size());
        System.out.println("Compare with : " + f33posts.size() +" strings ");
        skS.close();
        skS1.close();
        long time2 = System.currentTimeMillis();
        System.out.println("Build title: " + (time2 - time1));
        snoob.Vocabulary v = new snoob.Vocabulary();
        for(TitleStats f33title : f33posts){
            v.put(f33title.title);
        }
        long time3 = System.currentTimeMillis();
        System.out.println("Build vocabulary: " + (time3 - time2));
        TreeMap<Double,String> k = new TreeMap<>();
        long t = 0;
        for(TitleStats f33title : f33posts){
            long begin = System.currentTimeMillis();
            double s = v.score(f33title.title,test);
            t += System.currentTimeMillis() - begin;
            k.put(s,f33title.title);
        }
        long time4 = System.currentTimeMillis();
        NavigableMap<Double, String> kk = k.descendingMap();
        System.out.println("\n\nPuting time: ");
        int i =0;
        System.out.println("\n\nSearch string: " + test);
        System.out.println("Top 10 Similar string: ");
        for(Map.Entry<Double,String> e : kk.entrySet()){
            i++;
            System.out.printf("%-100s%s%n",e.getValue(),"Similar rating: " + e.getKey());
            if(i >10) break;
        }
        //System.out.println("Vocabulary count: " + (v.phrasesList.size() + v.wordList.size()));
        System.out.println("Compare with : " + f33posts.size() +" strings ");
        System.out.println("In: " + (t) + "ms");
    }
}