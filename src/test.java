

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args)
            throws IOException {
        Pattern pattern = Pattern.compile("/article/.*\\.html" +
                "|/user/npublic/\\?uid=.*" +
                "|list/[0-9]*\\?type=[0-9]" +
                "|\\?type=[0-9]&pn=[1-9].*");
        String url = "/user/npublic/?uid=8d67a19880ccc943e60e5ee7&pn=7";
        Matcher m = pattern.matcher(url);
        System.out.println(m.matches());

    }
}

