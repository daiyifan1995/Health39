

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
            throws Exception {
        System.out.println("9".compareTo("9"));





    }

    public static JsonObject parseSong(String url,String html){
        Document doc= Jsoup.parse(html);
        JsonObject jsonObject=new JsonObject();

        jsonObject.addProperty("url",url);

        Elements titleElements=doc.select("div[class='data__name']");
        if (titleElements.size()>0){
            String title=titleElements.text();
            jsonObject.addProperty("title",title);
        }

        Elements imgElements=doc.select("img[class='data__photo']");
        if (imgElements.size()>0){
            String coverImg=imgElements.get(0).attr("src");
            if (!coverImg.startsWith("https:")){
                coverImg="https:"+coverImg;
            }
            jsonObject.addProperty("coverImg",coverImg);
        }

        Elements singerElements=doc.select("div[class='data__singer']");
        if (singerElements.size()>0){
            JsonArray singers=new JsonArray();
            Elements singeritems=singerElements.get(0).select("a[href^=//y.qq.com/n/yqq/singer/]");
            if(singeritems.size()>0){
                for(Element singeritem:singeritems){
                    JsonObject singer=new JsonObject();
                    String singerName=singeritem.text().trim();
                    String singerUrl=singeritem.attr("href");
                    singer.addProperty("singerName",singerName);
                    singer.addProperty("singerUrl",singerUrl);
                    singers.add(singer);
                }
            }

            jsonObject.add("singer",singers);//添加的为string则是addproperty，否则直接为add
        }

        Elements infoElements=doc.select("li[class^=data_info]");
        if (infoElements.size()>0){
            JsonArray infos=new JsonArray();
            JsonObject info=new JsonObject();
            for(Element infoElement:infoElements){
                String item=infoElement.toString().replaceAll("<.*?>","");
                String parts[]=item.split("：",2);
                if(parts.length<2){
                    continue;
                }
                else{

                    Elements hrefs=infoElement.select("a");
                    if(hrefs.size()>0) {
                        JsonObject link=new JsonObject();
                        String infoUrl=hrefs.get(0).attr("href");
                        if (!infoUrl.startsWith("https:")){
                            infoUrl="https:"+infoUrl;
                        }
                        link.addProperty(parts[1],infoUrl);
                        info.add(parts[0],link);
                    }
                    else{
                        info.addProperty(parts[0],parts[1]);
                    }
                    infos.add(info);
                }
            }
            jsonObject.add("information",info);
        }

        Elements summaryElments=doc.select("div[class='about__cont']");
        if(summaryElments.size()>0) {
            String summary=summaryElments.get(0).text();
            jsonObject.addProperty("summary",summary);
        }

        Elements lyricElments=doc.select("div[class='lyric__tit']");
        if(lyricElments.size()>0) {
            String lyric=lyricElments.get(0).text();
            jsonObject.addProperty("lyric",lyric);
        }

        return jsonObject;

    }

    public static JsonObject parseAlbum(String url,String html){
        Document doc= Jsoup.parse(html);
        JsonObject jsonObject=new JsonObject();

        jsonObject.addProperty("url",url);

        Elements titleElements=doc.select("div[class='data__name']");
        if (titleElements.size()>0){
            String title=titleElements.text();
            jsonObject.addProperty("title",title);
        }


        Elements imgElements=doc.select("img[class='data__photo']");
        if (imgElements.size()>0){

            String coverImg=imgElements.get(0).attr("src");
            if (!coverImg.startsWith("https:")){
                coverImg="https:"+coverImg;
            }
            jsonObject.addProperty("coverImg",coverImg);
        }

        Elements singerElements=doc.select("div[class='data__singer']");
        if (singerElements.size()>0){
            JsonArray singers=new JsonArray();
            Elements singeritems=singerElements.get(0).select("a[href^=//y.qq.com/n/yqq/singer/]");
            if(singeritems.size()>0){
                for(Element singeritem:singeritems){
                    JsonObject singer=new JsonObject();
                    String singerName=singeritem.text().trim();
                    String singerUrl=singeritem.attr("href");
                    if (!singerUrl.startsWith("http")) {
                        singerUrl="https:"+singerUrl;
                    }
                    singer.addProperty("singerName",singerName);
                    singer.addProperty("singerUrl",singerUrl);
                    singers.add(singer);
                }
            }

            jsonObject.add("singer",singers);//添加的为string则是addproperty，否则直接为add
        }

        Elements infoElements=doc.select("li[class^=data_info]");
        if (infoElements.size()>0){
            JsonArray infos=new JsonArray();
            JsonObject info=new JsonObject();
            for(Element infoElement:infoElements){
                String item=infoElement.toString().replaceAll("<.*?>","");
                String parts[]=item.split("：",2);
                if(parts.length<2){
                    continue;
                }
                else{

                    Elements hrefs=infoElement.select("a");
                    if(hrefs.size()>0) {
                        JsonObject link=new JsonObject();
                        String infoUrl=hrefs.get(0).attr("href");
                        if (!infoUrl.startsWith("https:")){
                            infoUrl="https:"+infoUrl;
                        }
                        link.addProperty(parts[1],infoUrl);
                        info.add(parts[0],link);
                    }
                    else{
                        info.addProperty(parts[0],parts[1]);
                    }
                    infos.add(info);
                }
            }
            jsonObject.add("information",info);
        }

        Elements songElements=doc.select("span[class^=songlist__songname_txt]");
        if (songElements.size()>0){
            JsonArray songs=new JsonArray();
            for(Element songElement:songElements){
                JsonObject song=new JsonObject();
                String songName=songElement.text();
                String songUrl=songElement.select("a").get(0).attr("href");
                if (!songUrl.startsWith("http")) {
                    songUrl="https:"+songUrl;
                }
                song.addProperty(songName,songUrl);
                songs.add(song);
            }
            jsonObject.add("song",songs);
        }

        Elements summaryElements=doc.select("div[class^=about__cont]");
        if (summaryElements.size()>0){
            JsonArray songs=new JsonArray();
            String summary=summaryElements.get(0).toString().replaceAll("<.*?>","");

            jsonObject.addProperty("summary",summary);
        }


        return jsonObject;

    }

}

