import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class OpenHtml {
    public static Document openHtml(String filename)
            throws IOException {
        Document doc;
        try {
            File input = new File(filename);
            doc = Jsoup.parse(input, "UTF-8");

            return doc;
        } catch (IOException exc) {
            System.out.println(exc);
            doc = null;
            return doc;
        }

    }

    public static Document openHtmlurl(String url)
            throws IOException {
        Document doc;
        try {
            String filename=url.replace("https://","")
                    .replace("http:","")
                    .replace(".com","")
                    .replace(".cn","")
                    .replace("www.","")
                    .replace(".html","");

            String[] dirnames=filename.split("/");

            String dirname="";
            for(int i=0;i<dirnames.length-1;i++) {

                dirname=dirname+dirnames[i]+"\\";
            }

            File input = new File(dirname+dirnames[dirnames.length-1]+".html");
            doc = Jsoup.parse(input, "UTF-8");

            return doc;
        } catch (IOException exc) {
            System.out.println(exc);
            doc = null;
            return doc;
        }

    }
}
