package snoob;

/**
 * Created by Snoob on 4/14/2016.
 */
public class WordCount {
    String word;
    long count = 0;
    public WordCount(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordCount wordCount = (WordCount) o;

        return word.equals(wordCount.word);

    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    public void count(){count++;}
}
