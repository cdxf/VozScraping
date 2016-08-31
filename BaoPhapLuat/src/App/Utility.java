package App;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import net.ricecode.similarity.LevenshteinDistanceStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.w3c.dom.Node;

/**
 * Created by Snoob on 4/13/2016.
 */
public class Utility {
    static SimilarityStrategy  strategy = new LevenshteinDistanceStrategy();
    static StringSimilarityService  service = new StringSimilarityServiceImpl(strategy);
    public static double stringCompare(String src, String des){
        return service.score(src,des);
    }
    public static int getDeep(DomNode n){
        int deep = 0;
        DomNodeList<DomNode> childNodes = n.getChildNodes();
        if(childNodes.size() <2) return 0;
        else
        for(DomNode e : childNodes){
            deep = getDeep(e) > deep ? getDeep(e) : deep;
        }
        return deep +1;
    }
    public static String beautify(String src){
        return src.replaceAll("\n", "").replaceAll("\r", "").trim().replaceAll(" +", " ");
    }
    public static String getDeepestString(DomNode parent,int deep){
        if(deep < 0) return "";
        Node aClass = parent.getAttributes().getNamedItem("class");
        if(aClass!= null)
            if(aClass.getTextContent().equals("relative-post")) return "";
        int parentDeep = getDeep(parent);
        if(parentDeep <1) {
            if(false)return "";
            String content = parent.getTextContent().replaceAll(" ","").replaceAll("<br>","").trim();
            if (content.length()<15) return "";
            if(content.length()> 150) return "";
            return parent.getTextContent();
        }
        for(DomNode child : parent.getChildNodes()){
            String deepest = getDeepestString(child,deep-1);
            if(deepest.equals("")) continue;
            else return deepest;
        }
        return "";
    }
    public static String getDeepestText(DomNode node, int searchDeep){
        for(int i = 0;i<searchDeep;i++){
            node = node.getParentNode();
            String deepest = getDeepestString(node,3);
            if(deepest.equals("")) continue;
            return deepest;
        }
        return "";
    }
}
