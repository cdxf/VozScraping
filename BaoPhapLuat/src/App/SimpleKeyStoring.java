package App;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Snoob on 4/13/2016.
 */
public class SimpleKeyStoring<ObjectType> {
    private String filename;
    private boolean firstTime = false;
    private HashSet<ObjectType> keyList;
    public SimpleKeyStoring(String filename) throws IOException, ClassNotFoundException {
    this.filename = filename;
        File file = new File(filename);
        if(!file.exists() || file.isDirectory()){
            firstTime = true;
            keyList = new HashSet<>();
        }
        else{
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filename));
            keyList = (HashSet<ObjectType>) stream.readObject();
            stream.close();
        }

    }
    public boolean isFirstTime(){
        return firstTime;
    }
    public void close() throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filename));
        stream.writeObject(keyList);
    }
    public Set<ObjectType> getList(){
        return keyList;
    }
}
