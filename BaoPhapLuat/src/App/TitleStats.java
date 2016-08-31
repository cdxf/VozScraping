package App;

import java.io.Serializable;

/**
 * Created by Snoob on 4/13/2016.
 */
public class TitleStats implements Serializable{
    private static final long serialVersionUID = 64408070L;
    public String title = "";
    public Integer score;
    public TitleStats(String title, int views,int replies) {
        this.title = title;
        this.score = calcScore(views,replies);
    }
    private int calcScore(int views,int replies){
        return views + replies*100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TitleStats that = (TitleStats) o;

        return title.equals(that.title);

    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
