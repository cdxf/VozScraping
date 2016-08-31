package App;

import java.io.IOException;
import java.text.ParseException;
import java.util.Set;

/**
 * Created by Snoob on 4/13/2016.
 */
public interface IBot {
    PostInfo getPosts(String title) throws IOException, ParseException;
    Set<String> getTitle() throws IOException;
    String getColor();
    String getLogoURL();
}
