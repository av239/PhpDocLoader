import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexei Vasin
 */


public class PhpDocLoader {

    private static String url;
    private static String methodName;
    private static String methodSignature;
    private static String methodReturnType;
    private static List<String> parameters = new ArrayList<String>();
    private static List<String> descriptions = new ArrayList<String>();

    public static void main(String[] args) {
        url = "http://www.php.net/manual/en/memcache.replace.php";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("span[class]");
            for (Element link : links){
                if (link.hasClass("methodname")){
                    methodName = link.text();
                }
                if (link.hasClass("methodparam")){
                    parameters.add(link.text());
                }

            }
            links = doc.select("p[class]");
            for (Element link : links){
                if (link.hasClass("para")){
                    if (!link.text().isEmpty() && !link.text().contains("has been")){
                        descriptions.add(link.text());
                    }
                }
            }
            links = doc.select("div[class]");
            for (Element link : links){
                if (link.hasClass("methodsynopsis")){
                    methodSignature = link.text().replaceAll("\\[|\\]", "");
                    methodSignature = methodSignature.replaceAll("\\s{2,}", "\\s");
                    methodSignature = methodSignature.replaceAll("\\s,", ",");
                    methodReturnType = methodSignature.substring(0, methodSignature.indexOf(" "));
                }
            }
        } catch (IOException e) {

        }

        System.out.println(getPhpDoc());
    }

    public static String getPhpDoc(){
        StringBuffer result = new StringBuffer();
        result.append("/**\n");
        result.append("* " + descriptions.get(0)).append("\n");
        for (int i = 1; i < descriptions.size() - 1; i++){
            result.append("* @param ").append(parameters.get(i - 1)).append(" ").append(descriptions.get(i)).append("\n");
        }
        result.append("* @return ").append(methodReturnType).append(" ").append(descriptions.get(descriptions.size() - 1)).append("\n");
        result.append("* @link ").append(url).append("\n");
        result.append("*/\n");
        result.append(methodSignature).append(" {}");
        return result.toString();
    }
}
