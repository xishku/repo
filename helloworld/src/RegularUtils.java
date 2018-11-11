import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {

    public static Set<String> filterString(String src, String filter){
        Set<String> set = new HashSet<String>();
        Pattern pattern = Pattern.compile(filter);
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()){
            set.add(matcher.group(0));
        }

        return set;
    }
}
