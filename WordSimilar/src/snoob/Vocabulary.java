package snoob;

import App.Utility;

import java.util.*;
/**
 * Created by Snoob on 4/14/2016.
 */
public class Vocabulary {
    Integer totalWordCount = 0;
    Integer totalPhraseCount = 0;
    public ArrayList<Integer> wordCount = new ArrayList<>();
    public Map<String,Double> weightCache = new HashMap<>();
    public List<Map<String, Integer>> vocabulary= new ArrayList<Map<String,Integer>>();
    public Vocabulary(){
    }
    public Vocabulary(Set<String> phrases) {
        Set<String> words = new HashSet<>();
        for(String phrase : phrases){
                put(phrase);
            }
    }
    public void incCount(int wordDeep){
        if(wordCount.size() <= wordDeep){
            wordCount.add(0);
        }
        wordCount.set(wordDeep-1,wordCount.get(wordDeep-1)+1);
    }
    public Integer getCount(int wordDeep){
        if(wordCount.size() <= wordDeep){
            wordCount.add(0);
        }
        return wordCount.get(wordDeep-1);
    }

    public String normalize(String word){
        return word.toLowerCase().replaceAll("[\n\r‘’\\-!…/\\[\\]'“”:0-9,.\"?]", "");
    }
    public String buildWord(int pos,int pos2,List<String> split ){
        String buildWord = "";
        for(int i = pos;i<=pos2;i++){
            buildWord += split.get(i) + " ";
        }
        buildWord = buildWord.trim();
        return buildWord;
    }
    public ArrayList<String> toToken(String phrase){
        ArrayList<String> split = new ArrayList<>(Arrays.asList(Utility.beautify(phrase).split(" ")));
        ListIterator<String> iterator = split.listIterator();
        while(iterator.hasNext()){
            iterator.set(normalize(iterator.next()));
        }
        split.removeIf(""::equals);
        return split;
    }
    public void put(String phrase){
        List<String> split = toToken(phrase);
        for(int pos = 0; pos<split.size();pos++) {
            for(int pos2 = pos;pos2 < split.size();pos2++){
                String buildWord = buildWord(pos,pos2,split);
                int wc = pos2-pos;
                if(vocabulary.size() <= wc){
                    vocabulary.add(new HashMap<>());
                }
                if (vocabulary.get(wc).containsKey(buildWord)) {
                    vocabulary.get(wc).put(buildWord, vocabulary.get(wc).get(buildWord) + 1);
                } else {
                    vocabulary.get(wc).put(buildWord, 1);
                }
                incCount(wc+1);
            }

        }
    }
    public double weight(String key){
        if(!weightCache.containsKey(key)){
            double weight = 0;
            int wordDeep = 1;
            for( int i=0; i<key.length(); i++ ) {
                if( key.charAt(i) == ' ' ) {
                    wordDeep++;
                }
            }
            Integer count = vocabulary.get(wordDeep-1).get(key);
            if(count == null) weight= 1;
            else weight = (((double)getCount(wordDeep)/(count)-1)/(getCount(wordDeep)-1));
            //if(weight > 0.48) weight= 0;
            weightCache.put(key,weight);
        }
        return weightCache.get(key);
}
    public double score(String start, String end){
        List<String> startSet = toToken(start);
        List<String> endSet = toToken(end);
       double finalScore = 0;
        int maxLen = (startSet.size() > endSet.size() ? endSet.size() : startSet.size());
        double[] count = new double[maxLen];
        for(int i = 0; i <startSet.size();i++){
            for(int j = 0; j < endSet.size();j++){
                for(int length = 1; length < maxLen;length++){
                    if(i < startSet.size() - (length-1) && j < endSet.size() - (length-1)){
                        String word1 = buildWord(i,i+length-1,startSet);
                        String word2 = buildWord(j,j+length-1,endSet);
                        if(word1.equals(word2)) {
                            double weight = this.weight(word1);
                            count[length-1] += weight;
                        }
                        else{
                            break;
                        }
                    }
                }

            }
        }
        for(int posDif = 0; posDif < maxLen;posDif++) {
            double score = 0;
            int k = (startSet.size()-posDif)*(endSet.size()-posDif);
            if(k == 0){
                score = 0;
            }else{
                score = count[posDif]*(posDif+1)/k;
                //score = count[posDif]*(posDif+1);
                finalScore+= score;
            }
        }
        return finalScore;
        //normal way return (count*2000)/(startSet.size()*endSet.size());
    }
}
