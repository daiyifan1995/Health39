package src;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.DocFlavor;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;

public class Iask {

    public String downLoad(String url)
            throws IOException {
        Document doc = Jsoup.connect(url).timeout(5000).get();
        String filename=new String(doc.title())+".html";
        for(String i:url.split("/")){
            filename=i;
        }
        try(FileWriter file=new FileWriter(filename)){
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

    public Map<String,Object> getElement(Document doc)
        throws  IOException {
        Gson jsonReslt=new Gson();
        Map<String,Object> result =new HashMap<String,Object>();
        int answersCount=0;
        Map<String,Object> answers=new HashMap<String,Object>();
        List<Object> otherAnswers=new ArrayList<Object>();
        List<Object> goodAnswers=new ArrayList<Object>();
        try {
            result.put("url",getUrl(doc));
            result.put("path",getPath(doc));
            result.put("question",getQuestion(doc));
            goodAnswers=getGoodAnswers(doc);
            otherAnswers=getOtherAnswers(doc);
            answersCount=goodAnswers.size()+otherAnswers.size();
            answers.put("answersCount",answersCount);
            answers.put("goodAnswers",goodAnswers);
            answers.put("otherAnswers",otherAnswers);
            result.put("answers",answers);
            System.out.println(result);
        }
        catch (Throwable exc){
            System.out.println(exc);
        }
        return result;
    }

    public void writeresult(Map<String,Object> result,String writename,Boolean append){
        try(FileWriter output = new FileWriter(writename,append)){
            output.write(result.toString()+"\r\n");
        }
        catch (IOException exc){
            System.out.println(exc);
        }
    }

    private String getUrl(Document doc)//从head中得到url
            throws IOException {
        Element head=doc.select("head").first();
        String url=new String("");
        try{
            url="http://"+head.select("meta[http-equiv='mobile-agent']").first().attr("content").
                    replace("format=html5; url=http://m.","");
        }
        catch (Throwable exc){
            url="error";
        }
        return url;

    }

    private String getPath(Document doc)
            throws IOException {
        String path=new String("");
        try {

            Elements paths = doc.select("div.breadcast-fl");
            for (Element i : paths) {
                path = path + ">" + i.select("a").first().text();
            }
            if(path.equals("")){
                throw new IOException();
            }
        } catch (IOException exc) {
            path="ERROR";
        }

        return path;

    }

    private Map<String,String> getQuestion(Document doc)
            throws IOException {
        Map<String,String> question=new HashMap<String,String>();
        String questionTitle=new String();
        String questionText=new String();
        try {

            //if(doc.select("h1[class='question-title']").first()!=null)
            if(doc.select("h1[class='question-title']").size() !=0){

                questionTitle=doc.select("h1[class='question-title']").first().text();
                question.put("questionTitle",questionTitle);
            }
            if(doc.select("h2[class='question-title']").size() !=0){

                questionTitle=doc.select("h2[class='question-title']").first().text();
                question.put("questionTitle",questionTitle);
            }

            if(doc.select("pre[class='question-text']").size()!=0){
                if(!doc.select("pre[class='question-text']").first().text().equals("")){
                    questionText = doc.select("pre[class='question-text']").first().text();
                    question.put("questionText",questionText);}
            }

            if(questionText.equals("") && questionTitle.equals("")  ) {
                throw new IOException();
            }
        } catch (Throwable exc) {
            //可能有title/text，也可能没有
            //首先判断该标签是否存在，再判断该标签内是否有text
            try{

                if(doc.select("span[class='title-f22']").size() !=0){
                    if(!doc.select("span[class='title-f22']").first().text().equals("")){
                        questionTitle=doc.select("span[class='title-f22']").first().text();
                        question.put("questionTitle",questionTitle);}
                }

                if(doc.select("div[class='title-f22']").size() !=0) {
                    if(!doc.select("div[class='title-f22']").first().text().equals("")){
                        questionTitle=doc.select("div[class='title-f22']").first().text();
                        question.put("questionTitle",questionTitle);
                    }
                }

                if(doc.select("div[class='questionText']").size()!=0) {
                    questionText = doc.select("div[class='questionText']").first().
                        select("pre").last().toString();
                    Pattern pattern=Pattern.compile("<span.*</span>", DOTALL);
                    Matcher matcher=pattern.matcher(questionText);
                    if (matcher.find()) {
                        questionText=questionText.replace(matcher.group(),"").
                                replace("<pre>","").replace("</pre>","");
                    }
                //System.out.println(question_text);
                question.put("questionText",questionText);
                }
                if(questionText.equals("") && questionTitle.equals("")  ) {
                    throw new IOException();
                }

            }
            catch (Throwable exc1){
                question.put("ERROR",exc.toString());
            }
        }
        return question;
    }

    private List<Object> getOtherAnswers(Document doc)
            throws IOException {
        List answers=new ArrayList();
        try {
            Elements other_answers = doc.select("li[t='disploy']");
            for(Element i:other_answers){
                Map<String,String> answer=new HashMap<String,String>();
                String answerContent=i.select("div[class='new-answer-text new-answer-cut new-pre-answer-text']").
                        first().select("pre").first().text();
                answerContent.replace("全部","");
                answerContent.replace("&nbsp;","");
                answer.put("answerContent",answerContent);
                String answerTime=i.select("p[class='time']").text();
                answer.put("answerTime",answerTime);
                String likes=i.select("a[class='operation-ele btn-goods praise']").text();
                answer.put("likes",likes);
                String dislikes=i.select("a[class='operation-ele btn-bad step']").text();
                answer.put("dislikes",dislikes);
                answers.add(answer);

            }
        } catch (Throwable exc) {
            try{
                Elements other_answers = doc.select("li[t='disploy']");

                for(Element i:other_answers){

                    Map<String,String> answer=new HashMap<String,String>();
                    String answerContent=i.select("div[class='answerText']").
                            first().select("pre").last().toString();
                    Pattern pattern=Pattern.compile("<div.*</div>", DOTALL);
                    Matcher matcher=pattern.matcher(answerContent);
                    if (matcher.find()) {
                        answerContent=answerContent.replace(matcher.group(),"");
                    }
                    answerContent=answerContent.replace("</pre>","").
                            replace("<pre>","");
                    answer.put("answerContent",answerContent);
                    String answerTime=i.select("span[class='answer_t']").text();
                    answer.put("answerTime",answerTime);
                    String likes=i.select("span[class='praise mr15']").text();
                    answer.put("likes",likes);
                    String dislikes=i.select("span[class='praise1 mr15 step']").text();
                    answer.put("dislikes",dislikes);

                    answers.add(answer);
                }
            }catch (Throwable exc1){
                answers.add("ERROR");
                answers.add(exc1);
            }
        }
        return answers;

    }

    private List<Object> getGoodAnswers(Document doc)
            throws IOException {
        List answers=new ArrayList();
        try {
            Elements other_answers = doc.select("div[class='new-goods-answer']");
            if(other_answers.size()==0){
                throw new IOException();
            }
            for(Element i:other_answers){
                Map<String,String> answer=new HashMap<String,String>();
                String answerContent=i.select("div[class='new-answer-text new-answer-cut new-pre-answer-text']").
                        first().select("pre").last().text();

                answerContent.replace("全部","");
                answer.put("answerContent",answerContent);
                String answerTime=i.select("p[class='time']").text();

                answer.put("answerTime",answerTime);
                String likes=i.select("a[class='operation-ele btn-goods praise']").text();
                answer.put("likes",likes);
                String dislikes=i.select("a[class='operation-ele btn-bad step']").text();
                answer.put("dislikes",dislikes);
                answers.add(answer);
            }
        } catch (Throwable exc) {
            try{
                Elements other_answers = doc.select("div[class='good_answer']");

                for(Element i:other_answers){
                    Map<String,String> answer=new HashMap<String,String>();
                    String answerContent=i.select("div[class='answer_text']").
                            first().select("pre").last().text();

                    answerContent.replace("全部","");
                    answer.put("answerContent",answerContent);
                    String answerTime=i.select("span[class='time mr10']").text().replace("|","");
                    answer.put("answerTime",answerTime);
                    String likes=i.select("span[class='praise mr15']").text();
                    answer.put("likes",likes);
                    String dislikes=i.select("span[class='praise1 mr15 step']").text();
                    answer.put("dislikes",dislikes);
                    Map<String,String> user_card=new HashMap<String,String>();
                    //String location=i.select("p[class='clearfix']").first().text();
                    //System.out.println(location);
                    answers.add(answer);
                }

            }
            catch (Throwable exc1){
                    System.out.println(exc1);
                }

            }

            return answers;

    }

}

