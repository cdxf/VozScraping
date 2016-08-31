package App;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Created by Snoob on 4/13/2016.
 */
public class PostInfo {
    public String title ="";
    public String url ="";
    public PostDetail details;
    public PostInfo(String title, String url) {
        this.title = title;
        this.url = url;
    }
    public String toBBCODE(){
        String contents = "";
        contents += "[QUOTE]\n";
        int division = 0;
        if(details.contentImgs.size() != 0) division = details.contents.size()/ details.contentImgs.size();
        contents += "[B]" + details.contents.get(0) + "[/B]\n" ;
        for(int i = 1;i<details.contents.size();i++){
            contents+= details.contents.get(i) + "\n\n";
            if(division != 0)
            if((i+1)%division == 0){
                int e = (i+1)/division -1;
                if(details.contentImgs.size()> e) {
                    ContentImg ci = details.contentImgs.get(e);
                    contents += "[CENTER][IMG]" + ci.url + "[/IMG]\n";
                    contents += "[I]" + ci.description + "[/I]" + "[/CENTER]\n\n";
                }
            }
        }
        Locale vn = Locale.US;
        for(Locale e : DateFormat.getAvailableLocales()){
            if(e.getCountry().equals("VN")){
                vn = e;
                break;
            }
        }
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, vn);
        contents += "[B][RIGHT]"+ details.Author+"\n";
        contents += df.format(details.date) + "[/RIGHT][/B]\n";
        contents += "[/QUOTE]\n";
        contents +=
                "[RIGHT][INDENT][URL=\"" + url + "\"]\n" +
                "[IMG]http://gagiongdabaco.com.vn/images/2015/06/17/logo.png[/IMG]\n" +
                "[/URL]\n" +
                "[I][B][COLOR=\"#9A0909\"]This thread is brought to you by BaoPhapLuat Bot 1.1\n" +
                "[/COLOR][/B][/I][/INDENT][/RIGHT]";
        return contents;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostInfo postInfo = (PostInfo) o;

        return title.equals(postInfo.title);

    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
