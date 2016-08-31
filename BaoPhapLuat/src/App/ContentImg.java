package App;

/**
 * Created by Snoob on 4/13/2016.
 */
public class ContentImg {
    public String url;
    public String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentImg that = (ContentImg) o;

        if (!url.equals(that.url)) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public ContentImg(String url, String description) {

        this.url = url;
        this.description = description;
    }
}
