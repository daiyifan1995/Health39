package src;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class WuKKong {
    public String downLoad(String url)
            throws IOException {
        Document doc = Jsoup.connect(url)
                .header("cookie","BIDUPSID=E9E45583BD76E5A9AB847071F5E1CCE2; PSTM=1539700175; BAIDUID=66258A5EAAC75CDC725178D0565C0696:FG=1; isMetisClick=click; PREV_DAILY_PERIOD=2001; BDRCVFR[r3UZWEoDlR0]=9xWipS8B-FspA7EnHc1QhPEUf; delPer=0; PSINO=2; H_PS_PSSID=; Hm_lvt_6859ce5aaf00fb00387e6434e4fcc925=1550656531,1550657126,1550657540,1550711781; IKUT=1344; dis_qids=1712551351%2C1336135360%2C1714931926%2C1714932669%2C1712335140%2C1717852948%2C1706130584%2C1714932167%2C1707561989; ZD_ENTRY=empty; Hm_lpvt_6859ce5aaf00fb00387e6434e4fcc925=1550711916")
                .timeout(5000)
                .post();
        String filename=new String(doc.title())+".html";

        for(String i:url.split("/")){
            filename=i;
        }
        filename="123";
        try(FileWriter file=new FileWriter(filename+".html")){
            file.write(doc.toString());
        }
        catch(IOException exc){
            System.out.println(exc);
        }

        System.out.println(filename);
        return filename;
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

    public Map<String,Object> parseHtml(Document doc)
            throws  IOException {
        Map<String,Object> result=new HashMap<>();
        List<String> tag=new ArrayList<>();
        String url=new String();
        String questionName=new String();
        String questionText=new String();
        String questionCollection=new String();
        String answerCount=new String();
        List<Object> answerItems = new ArrayList<>();
        try {
            url=questionId(doc);
            result.put("Url",url);

            tag=getTag(doc);
            result.put("Tags",tag);

            questionName=getQuestionName(doc);
            result.put("questionName",questionName);

            questionText=getQuestionText(doc);
            if (!questionText.equals("")){
                result.put("questionText",questionText);
            }

            questionCollection=getQuestionCollection(doc);
            result.put(" questionCollection", questionCollection);

            answerCount=getAnswerCount(doc).replace("个回答","");
            result.put(" answerCount", answerCount);

            answerItems=getAnswerItem(doc);
            result.put("answerItem",answerItems);




            System.out.println(result);
        }
        catch (Throwable exc){
            System.out.println(exc);
        }
        return result;
    }

    private String questionId(Document doc)//从head中得到url
            throws IOException {
        Element head=doc.select("head").first();
        String url=new String("");
        try{
            url=head.select("meta[property='og:url']").first().attr("content");
        }
        catch (Throwable exc){
            url="error";
        }
        return url;

    }

    private List<String> getTag(Document doc)
            throws IOException {
        List<String> tag=new ArrayList<>();
        try {

            Elements tags = doc.select("div.question-tags").first().select("a");
            for (Element i : tags) {
                tag.add(i.text());
            }

        } catch (Throwable exc) {
            tag.add("ERROR");
        }

        return tag;
    }

    private String getQuestionName(Document doc)
            throws IOException {
        String questionName=new String();
        try {

            Elements questionNames = doc.select("h1.question-name");

            if(questionNames.size()!=0) {
                questionName=questionNames.first().text();
            }
            else{
                throw new IOException();
            }

        } catch (Throwable exc) {
            questionName="questionNameError";
        }

        return questionName;
    }

    private String getQuestionText(Document doc)
            throws IOException {
        String questionText=new String();
        try {

            Elements questionTexts = doc.select("div.question-text");

            if(questionTexts.size()!=0) {
                questionText=questionTexts.first().text();
            }

        } catch (Throwable exc) {
            questionText="";
        }

        return questionText;
    }

    private String getQuestionCollection(Document doc)
            throws IOException {
        String questionCollection=new String();
        try {

            questionCollection= doc.select("span.count").first().text();

        } catch (Throwable exc) {
            questionCollection="questionCollectionError";
        }

        return questionCollection;
    }

    private String getAnswerCount(Document doc)
            throws IOException {
        String answerCount=new String();
        try {

            answerCount= doc.select("h3.answer-count-h").first().text();

        } catch (Throwable exc) {
            answerCount="answerCountError";
        }

        return answerCount;
    }

    private List<Object> getAnswerItem(Document doc)
            throws IOException {
        List<Object> answerItems = new ArrayList<>();
        try {

            Elements answers= doc.select("div[data-node='answer-item']");

            for(Element i :answers){
                Map<String,Object> answerItem=new HashMap<>();
                String answerContent =i.select("p").toString();

                List<String> imgs=new ArrayList<>();
                Pattern pattern=Pattern.compile("<img src=\"(.*?)\" ");
                Matcher matcher=pattern.matcher(answerContent);
                while (matcher.find()) {
                    imgs.add(matcher.group(1));
                }
                if(imgs.size()>0){
                    answerItem.put("imgs",imgs);
                }

                answerContent=answerContent.replaceAll("<.*?>","");
                answerItem.put("answerContent",answerContent);

                String answerTime=i.select("a.answer-user-tag").first().text();
                answerItem.put("answerTime",answerTime);

                String answerLikes=i.select("span.like-num").first().text();
                answerItem.put("answerLikes",answerLikes);

                String answerComments=i.select("a[class='show-comment']").first().text();
                if (answerComments.equals("评论"))
                    answerComments="0评论";
                answerItem.put("answerComments",answerComments);


                answerItems.add(answerItem);
            }

        } catch (Throwable exc) {
            answerItems.add(" answerTopError");;
        }

        return answerItems;
    }

    public void writeresult(Map<String,Object> result,String writename,Boolean append){
        try(FileWriter output = new FileWriter(writename,append)){
            output.write(result.toString()+"\r\n");
        }
        catch (IOException exc){
            System.out.println(exc);
        }
    }
}
