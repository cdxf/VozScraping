import java.io.FileNotFoundException;

/**
 * Created by Snoob on 4/12/2016.
 */
public class AccountManager {
    public AccountManager(FileEntryWriter resultFile) throws Exception {
        while(true) {
            String line = null;
            while (line == null) {
                line = resultFile.get();
                Thread.sleep(5000);
            }
            String[] account = line.split(" @.@-@.@ ");
            String user = account[0];
            String password = account[1];
            System.out.println(user);
            System.out.println(password);
            AccountPoster poster = new AccountPoster(user,password);
            Thread.sleep(1800000);
        }
    }
}
