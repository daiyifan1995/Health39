import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaidujingyanAtricleAnalyse {
    public static void main(String[] args)
            throws IOException {
////        baiduJingyan J=new baiduJingyan();
////        J.downLoad("https://jingyan.baidu.com/article/215817f73a0a941eda14238c.html");
//        String filename="baiduJingyanData\\article\\ac6a9a5e4222292b653eac07.html";
//        baidujingyanAtricleAnalyse A = new baidujingyanAtricleAnalyse();
//        Map<String,Object> result=A.analysehtml(filename);
//        System.out.println(result);

        BaidujingyanAtricleAnalyse A = new BaidujingyanAtricleAnalyse();

        String dirname="baiduJingyanData\\article\\";
        File dir = new File(dirname);
        String[] children = dir.list();

        for(String filename:children) {
            filename = dirname + filename;

            Map<String, Object> result = A.analysehtml(filename);
            System.out.println(result);

            writeresult.writetojson(result,"test.json",true);
        }

        //要将问题输出为json文件

    }

    public Map<String,Object> analysehtml(String filename)
            throws IOException{
        BaidujingyanAtricleAnalyse A = new BaidujingyanAtricleAnalyse();

        Map<String,Object> result=new HashMap<>();

        Pattern pattern = Pattern.compile("<.*?>");

        Document doc = OpenHtml.openHtml(filename);


        //System.out.println(doc.select("video[class='jw-video jw-reset']").toString());

        String url=filename.replace("baiduJingyanData\\article\\",
                "https://jingyan.baidu.com/article/");
        result.put("url",url);

        try{
            String title = doc.title()
                    .replace("_百度经验", "");
            result.put("title",title);
        }
        catch (Throwable e){
            System.out.println(filename+"Title Error");
            System.out.println(e);
        }

        try{
            String path=A.getPath(doc,pattern);
            result.put("path",path);
        }
        catch (Throwable e){
            System.out.println(filename+"Path Error");
            System.out.println(e);
        }

        try{
            String scans=doc.select("ul[class='exp-info']").
                    select("span[class='views']").text();

            String time=doc.select("ul[class='exp-info']")
                    .select("time").text();
            if(scans.equals("") && time.equals("")){
                result.put("Video",A.getVideos(doc));
            }
            else{
                result.put("scans",scans);
                result.put("time",time);
            }

        }
        catch (Throwable e){
                System.out.println(filename+"Video Error");
                System.out.println(e);
        }



//        //
//        String video= doc.selectFirst("div[class='clearfix feeds-video-box']").toString();
//        只有video的id，无连接
        try{
            String summary=doc.select("div[class='content-listblock-text']")
                    .toString()
                    .replace("</p>","\\n")
                    .replaceAll("<.*?>","")
                    .replaceAll("\\s*","");
            result.put("summary",summary);
        }
        catch (Throwable e){
            System.out.println(filename+"Summary Error");
            System.out.println(e);
        }

        try{
            List<Map<String,Object>> content=A.getContent(doc,url);
            result.put("content",content);
        }
        catch (Throwable e){
            System.out.println(filename+"Content Error");
            System.out.println(e);
        }

        try{
            String likes=doc.select("span[class='like-num']").text();
            result.put("likes",likes);//点踩数没有
        }
        catch (Throwable e){
            System.out.println(filename+"Likes Error");
            System.out.println(e);
        }

        try{
            String votes=doc.select("span[id='v-a-num']").text();
            result.put("votes",votes);
        }
        catch (Throwable e){
            System.out.println(filename+"Votes Error");
            System.out.println(e);
        }



        return result;

    }


    public String getPath(Document doc,Pattern pattern)
            throws IOException{

        String path = doc.select("div[class='bread-wrap']").first().toString();

        Matcher matcher = pattern.matcher(path);

        while (matcher.find()) {
            String m = matcher.group();
            path = path.replace(matcher.group(), ">");
        }

        path = path.trim().replaceAll("\\s*", "")
                .replaceAll("百度经验", "")
                .replaceAll("&nbsp;&gt;&nbsp;", "")
                .replaceAll(">>", "")
                .replaceAll("\r", "");

        if (path.substring(path.length() - 1).equals(">")) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.substring(0, 1).equals(">")) {
            path = path.substring(1, path.length());
        }

        return path;
    }

    public List<Map<String,Object>> getContent(Document doc,String url)
            throws IOException{

        Pattern pattern=Pattern.compile("data-src=\"(.*?)\"");

        List<Map<String,Object>> contents=new LinkedList<>();

        Elements modules=doc.select("div[class='exp-content-block']");

        for(Element module:modules){
            Map<String,Object> content=new HashMap<>();

            String head=module.select("h2[class='exp-content-head']").text();

//            if(head.equals("")){
//                continue;
//            }

            content.put("head",head);


            Elements elements=module.select("li");

            List<Map<String,Object>> steps=new LinkedList<>();
            if (head.equals("")){
//            if (elements.size()!=0){

                for(Element element :elements) {

                    Map<String,Object> step=new HashMap<>();

                    String order=element.attr("class")
                            .replace("exp-content-list list-item-","");
                    String text;
                    try{
                        text=element.select("div[class='content-list-text']")
                                .toString()
                                .replaceAll("\\s*","")
                                .replaceAll("<br>|</p>","\\n")
                                .replaceAll("步骤阅读|<.*?>","");
                    }
                    catch (NullPointerException e){
                        text="";
                    }
                    step.put("text",text);
                    step.put("step",order);


                    String imgs=element.select("img").toString();
                    List<String> imglist=new LinkedList<>();
                    Matcher matcher=pattern.matcher(imgs);
                    while (matcher.find()) {
                        imglist.add(matcher.group(1));
                    }
                    if(imglist.size()!=0)
                        step.put("jpg",imglist);

                    if(!step.toString().equals("{}"))
                        steps.add(step);

                }
            }
            else{
                elements=module.select("div[class='exp-content-listblock']");
                if (elements.size()!=0){
                    Integer order=0;
                    for(Element element :elements){

                        Map<String,Object> step=new HashMap<>();

                        String text;

                        try{
                            text=element.select("div[class='content-listblock-text']")
                                    .toString()
                                    .replaceAll("\\s*","")
                                    .replaceAll("<br>|</p>","\\n")
                                    .replaceAll("步骤阅读|<.*?>","");
                        }
                        catch (NullPointerException e){
                            text="";
                        }
                        order++;
                        step.put("text",text);
                        step.put("step",order);


                        String imgs=element.select("img").toString();
                        List<String> imglist=new LinkedList<>();
                        Matcher matcher=pattern.matcher(imgs);
                        while (matcher.find()) {
                            imglist.add(matcher.group(1));
                        }
                        if(imglist.size()!=0)
                            step.put("jpg",imglist);

                        if(!step.toString().equals("{}"))
                            steps.add(step);

                    }
                }
            }


            if(steps.size()!=0){
                content.put("steps",steps);
            }

            contents.add(content);
        }

        return contents;
    }

    public Map<String,String> getVideos(Document doc)
            throws IOException {

        Map<String, String> video = new HashMap<>();

        String Vid = doc.select("li[class='pt-10 pb-10 feeds-video-list-item feeds-video-list-item-0 clearfix current']")
                .attr("vid");

        System.out.println(Vid);

        String x=String.format("!function\\(\\).*videoList:\\[(\\{\"feedVid\":\"%s.*?}).*?\\].*function\\(\\)",Vid);
        Pattern p=Pattern.compile(x);
        Matcher m = p.matcher(doc.toString().replaceAll("\\s*", ""));
        while (m.find()) {
            System.out.println(m.group(1));
            String map = m.group(1);
            for (String i : map.split(",")) {
                if (i.startsWith("\"playUrl\"")) {
                    String videoSrc = i.replace("\"playUrl\":\"", "");
//                            .replace("\"", "")
//                            .replace("\\", "");
                    video.put("VideoSrc", videoSrc);
                }
                if (i.startsWith("\"videoView\"")) {
                    String views = i.replace("\"videoView\":", "")
                            .replace("}", "");
                    video.put("views", views);
                }
            }
        }
        return video;
    }

    public void writeresult(Map<String,Object> result,String writename,Boolean append){
        try(FileWriter output = new FileWriter(writename,append)){
            JSONObject json = new JSONObject(result);
            output.write(json+"\r\n");
        }
        catch (IOException exc){
            System.out.println(exc);
        }
    }

}
