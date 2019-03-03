package src;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class Health {
    String filename;
    Document doc;
    String url;
    List outs=new ArrayList();


    Health(String filename)
    {
        this.filename=filename;
    }

    private void openhtml()//打开html文件
    {
        try{
            File input = new File(filename);//使用jsoup的方法直接打开html文件
            doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        }
        catch (IOException exc) {System.out.println(exc);}
        }

    private void geturl(Document doc)//从head中得到url
    {
        try{url="http://"+doc.select("meta").first().nextElementSibling().
                attr("content").
                replace("format=html5; url=http://wap","");}
        catch (Throwable exc){url="error";}

    }

    public void analysetxt()
            throws IOException{
        Document ss = Jsoup.parse("");;
        try(BufferedReader br=new BufferedReader(new FileReader(filename))){
            String s;
            while((s=br.readLine())!=null){

                ss = Jsoup.parse(s);
                outs.add(analyse(ss));
            }
        }
        catch (Throwable exc){
            System.out.println(exc);

        }
    }

    private Map analyse(Document doc)
            throws IOException {

        Map result = new HashMap();

        String html=new String("");
        geturl(doc);
        result.put("url",url);
        getpath(doc,result);
        getquestion(doc,result);
        getusers_info(doc,result);
        getask_time(doc,result);
        getkeywords(doc,result);
        getanswerss(doc,result);

        return result;

    }


    private void getpath(Document doc,Map result)
            throws IOException {
        try{
            Elements paths =doc.select("div.sub");//集合使用Elements，单个使用Element
            String path=new String(paths.text());
            result.put("path",path);
        }
        catch (Throwable exc)
        {result.put("ERROR_path",exc);}

    }

    private void getquestion(Document doc,Map result)
            throws IOException{
        try{
            String question=new String(doc.select("p.txt_ms").text());
            result.put("question",question);
        }
        catch (Throwable exc){        {result.put("ERROR_question",exc);}}

    }

    private void getusers_info(Document doc,Map result)
            throws IOException {
        try{
        Map user=new HashMap();
        Elements users_info=doc.select("p.mation").first().select("span");
        String sex=users_info.first().text();
        String age=users_info.first().nextElementSibling().text();
        String time=users_info.first().lastElementSibling().text();
        user.put("sex",sex);
        user.put("age",age);
        user.put("fabing_time",time);
        result.put("user_info",user);

    }
        catch (Throwable exc){        {result.put("ERROR_user_info",exc);}}
    }

    private void getask_time(Document doc,Map result)
            throws IOException {
        try{
            String ask_time=doc.select("p.txt_nametime").first().select("span")
                    .first().nextElementSibling().text();
            result.put("ask_time",ask_time);

        }
        catch (Throwable exc){        {result.put("ERROR_ask_time",exc);}}
    }

    private void getkeywords(Document doc,Map result)
            throws IOException {
        try{
            Elements kw =doc.select("p.txt_label").first().select("span");

            List keywords= new ArrayList();
            for (Element i : kw)
                keywords.add(i.text());
            result.put("keywords",keywords);

        }
        catch (Throwable exc){        {result.put("ERROR_keywords",exc);}}
    }

    private void getanswerss(Document doc,Map result)
            throws IOException {
        List answers=new ArrayList();
        try{

            Elements doctors=doc.select("div.doctor_all");


            for(Element i :doctors)
            {
                Map answer_sheet=new HashMap();
                Element p=i.select("p.doc_xinx").first();
                Map doc_info=new HashMap();

                String doc_name=p.select("span").first().text();
                doc_info.put("doc_name",doc_name);
                String doc_postion=p.select("span").first().nextElementSibling().text();
                doc_info.put("doc_postion",doc_postion);
                String doc_loc=p.select("span").first().lastElementSibling().text();
                doc_info.put("doc_loc",doc_loc);

                answer_sheet.put("doc_info",doc_info);

                String doc_answer=i.nextElementSibling().text();
                answer_sheet.put("doc_answer",doc_answer);

                String answer_time=i.lastElementSibling().select("p.doc_time").first().text();
                answer_sheet.put("doc_time",answer_time);
                answers.add(answer_sheet);
            }

            result.put("answers",answers);
        }
        catch (Throwable exc){        {result.put("ERROR_answers",exc);}}
    }


    public void write(String writename)
    {
        try{

            FileOutputStream f=new FileOutputStream(writename);
            ObjectOutputStream o=new ObjectOutputStream(f);
            o.writeObject(outs);
            o.close();
            f.close();
        }
        catch(Throwable exc)
        {System.out.println(exc);}
    }

    public void read(String writename)
    {
        List e=new ArrayList();
        try {
            FileInputStream fileIn = new FileInputStream(writename);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            e = (List) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("not found");
            c.printStackTrace();
            return;
        }
        for(int i=0;i<e.size();i++)
        {
            System.out.println(e.get(i));
        }
    }

    public void writejson(String writename)
            throws IOException {


        // try-with-resources statement based on post comment below :)
        try (FileWriter file = new FileWriter(writename)) {
            for(int i=0;i< outs.size();i++)
                file.write(outs.get(i).toString()+"\r\n");
        }
    }

}

