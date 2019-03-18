import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SouGoubaike {
    //ip限制


    public static void main(String[] args)
    throws Exception {

        final String historyPrefix="https://baike.sogou.com/ShowHistory.e?sp=l";
        final int period=1000;

        final Pattern userPattern=Pattern.compile("/usercenter/home.v\\?uid=(.*)");
        final Pattern itemPattern=Pattern.compile("/v(.*)\\.htm");


        Set<String> items=SouGoubaike.selectItems(OpenHtml.openHtml("baike.sogou.html").toString(),itemPattern);
        Set<String> users=SouGoubaike.selectItems(OpenHtml.openHtml("baike.sogou.html").toString(),userPattern);



        for (String item:items){
            System.out.println(item);
            String url=historyPrefix+item;
            String docLast="";
        for (int i=1;i<=1000;i++){
                String itemUrl=url+"&p="+i;
                String filename=DownHtml.createfile(itemUrl);
                if (filename==null) {
                    docLast=OpenHtml.openHtmlurl(itemUrl).toString();
                    continue;
                }
                Thread.sleep(period * 10);
                String doc = Jsoup.connect(itemUrl).timeout(50000).ignoreContentType(true).execute().body();
                if (doc.equals(docLast)){
                    break;
                }
                docLast=doc;

                DownHtml.savehtml(doc,filename);

                Set<String> newItems=SouGoubaike.selectItems(doc,itemPattern);
                items.addAll(newItems);

                Set<String> newUsers=SouGoubaike.selectItems(doc,userPattern);
                users.addAll(newUsers);
            }
        }
    }

    private static Set<String> selectItems(String html,Pattern pattern)
            throws IOException{

        Set<String> itemSet=new HashSet<>();

        Document doc= Jsoup.parse(OpenHtml.openHtml("baike.sogou.html").toString());

        Elements itemElements=doc.select("a");
        for(Element itemElement:itemElements){
            String item=itemElement.attr("href");
            Matcher itemMatcher=pattern.matcher(item);
            if(itemMatcher.find()==true){
                itemSet.add(itemMatcher.group(1));
            }
        }
        return itemSet;
    }

}
