import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QQMusicSinger {
    public static void main(String[] args)
            throws Exception{
        Pattern idPattern=Pattern.compile("singermid_(.*?)&utf8_1");
        String filename="SingerUrl20190318123929";
        //QQMusicSinger.downLoadSingerInfo(filename);
        String dirname="C:\\Users\\daiyifan\\IdeaProjects\\Health39\\c.y.qq\\splcloud\\fcgi-bin\\fcg_get_singer_desc.fcg";
        File file=new File(dirname);
        String[] childen=file.list();
        if(childen.length==0)
            return;
        for (String child:childen){
            Document doc=OpenHtml.openHtml(dirname+"\\"+child);
            System.out.println(selectSingerInfo(doc,idPattern,child));
        }
    }

    public static void downLoadSingerInfo(String filename)
            throws Exception{
        File f=new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String id;
        while (( id= reader.readLine()) != null){
            DownHtml.downLoadQqSinger(id);
        }
    }

    public static Map<String, String> selectSingerInfo(Document doc, Pattern idPattern, String child)
            throws Exception{
        Matcher idMatcher=idPattern.matcher(child);
        Map<String,String> map=new HashMap<>();

        if (idMatcher.find()){
            map.put("singerId",idMatcher.group(1));
        }

        Elements itemElements=doc.select("item");
        if(itemElements.size()>0){
            for(Element itemElement:itemElements){
                Elements keyElements=itemElement.select("key");
                Elements valueElements=itemElement.select("value");

                if(keyElements.size()>0 && valueElements.size()>0){

                    String key=keyElements.get(0).text();
                    String value=valueElements.get(0).text().replace("\n","\\u0001|");
                    if(key.equals("") || value.equals("")){
                        continue;
                    }
                    map.put(key,value);

                }
            }
        }
        Elements otherInfoElements=doc.select("desc");
        if (otherInfoElements.size()>0){
            String info=otherInfoElements.get(0).text();
            info=info.replace("<![CDATA[","").replace("]]>","").replace("\n","\u0001|").trim();
            if(info.equals("")){

            }
            map.put("OtherInfo",info);
        }
        if(map.toString().equals("{}")){

        }
        return map;

    }



}
