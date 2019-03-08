

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class BaiduJingyan {

    public Document downLoad(String url,String ip,String port)
            throws Throwable {
        Document doc=new Document("");
        String dirname = "baiduJingyanData";
        String base="https://jingyan.baidu.com";

        String filename=url.replace(base,"")
                .replace("npublic/","npublic")
                .replace("?","_")
                .replace("=","_")
                .replace("&","_")
                .replace(".html","")
                .replace(" ","");

        filename=dirname + "\\" + filename + ".html";
        File f=new File(filename);

        if (!f.exists()) {
            DownHtml h=new DownHtml();
            doc=h.downLoad(url,filename);
        }
        else{
            doc=openHtml(filename);
        }
        return doc;
    }

    public Document openHtml(String filename)
            throws IOException {
        Document doc;
        try{
            File input = new File(filename);
            doc = Jsoup.parse(input, "UTF-8");
            return doc;
        }
        catch(IOException exc){
            System.out.println(exc);doc=null;return doc;
        }

    }

    public Set<String> selecturl(Document doc)
            throws IOException{
        Set<String> urlList=new HashSet<String>();
        String base="https://jingyan.baidu.com/";
        Pattern pattern=Pattern.compile("/article/.*\\.html" +
                "|/user/npublic/\\?uid=.*" +
                "|list/[0-9]*\\?type=[0-9]" +
                "|\\?type=[0-9]&pn=[1-9].*");
        Elements elements=doc.select("a");
        for (Element x:elements){
            String url=x.attr("href");
            Matcher matcher=pattern.matcher(url);
            if (matcher.find()) {
                url=base+matcher.group();
                if(url.contains("article")||url.contains("list")||url.contains("user"))
                    urlList.add(url);
                else{
                    String rep;
                    try{
                        rep=doc.selectFirst("li[class='on']").selectFirst("a").attr("href");
                    }
                    catch (Throwable e){
                        rep=doc.selectFirst("li[class='last on']").selectFirst("a").attr("href");
                    }
                    Matcher mat=Pattern.compile("(.*)\\?type=[0-9].*").matcher(rep);
                    while (mat.find()) {
                        url=url.replace("https://jingyan.baidu.com","");
                        //System.out.println("https://jingyan.baidu.com"+mat.group(1)+url);
                        urlList.add("https://jingyan.baidu.com"+mat.group(1)+
                                url.replace("/","")
                                        .replace("https:jingyan.baidu.com",""));
                    }

                }
               continue;
            }
        }
        return urlList;
    }

    public String[] dirList(String dirname)
            throws IOException {
        File dir = new File(dirname);
        String[] children = dir.list();
        if (children == null) {
            System.out.println("该目录不存在");
        } else {
            for (int i = 0; i < children.length; i++) {
                String filename = children[i];
            }
        }
        return children;
    }
}
