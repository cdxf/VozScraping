package App;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by Snoob on 4/14/2016.
 */
public class CustomCallable implements Callable<Set<TitleStats>> {
    int i = 0;
    int thread = 0;
    int start = 0;
    int end = 0;
    int forum = 0;
    public CustomCallable(int i,int start,int end, int thread,int forum) {
    this.i = i;this.thread = thread;this.start = start;this.end = end;this.forum=forum;
    }
    @Override
    public Set<TitleStats> call() throws Exception {
        int numPerThread = (end-start+1)/thread;
        Set<TitleStats> result = new HashSet<>();
        if(i == thread-1){
            result.addAll(F33TitleScraper.get(i*numPerThread,end,forum));
        }
        else{
            result.addAll(F33TitleScraper.get(i*numPerThread,(i+1)*numPerThread-1,forum));
        }
        return result;
    }
}
