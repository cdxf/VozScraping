/**
 * Created by Snoob on 4/11/2016.
 */
public class ProxyInfo {
    public String ProxyURL;
    public long expire = 0;
    public int nFail = 0;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProxyInfo proxyInfo = (ProxyInfo) o;

        if (expire != proxyInfo.expire) return false;
        return ProxyURL.equals(proxyInfo.ProxyURL);

    }

    @Override
    public int hashCode() {
        int result = ProxyURL.hashCode();
        result = 31 * result + (int) (expire ^ (expire >>> 32));
        return result;
    }
}