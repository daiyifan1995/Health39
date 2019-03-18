

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import javax.print.Doc;
import java.io.*;
import java.net.*;
import java.util.*;

import static com.google.gson.internal.bind.TypeAdapters.URL;
import static com.google.gson.internal.bind.TypeAdapters.newFactoryForMultipleTypes;

public class DownHtml {

    ArrayList<String> ips=new ArrayList<>();
    ArrayList<String> ports=new ArrayList<>();

    DownHtml()
            throws IOException{
        proxy x=new proxy();
        ips=x.ips;
        ports=x.ports;


    }

    public static void main(String[] args)
            throws Exception{
        //String url="https://jingyan.baidu.com/article/a681b0de11bc153b18434682.html";
        //System.out.println(URLDecoder.decode(url,"utf8"));
        File f=new File("SingerUrl20190318123929");
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String id;
        while (( id= reader.readLine()) != null){
            System.out.println(id);
            DownHtml.downLoadQqSinger(id);
        }

//        String id="003QbhX00B2P5y";
//        for (int i=0;i<=100;i++){
//            DownHtml.downLoadQqSinger(id);
//        }
     }


    public  static String downLoadQqSinger(String id)//根据url格式存储数据，对应openhtmlurl
            throws Exception {
        String  doc;

        String url="https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_singer_desc.fcg?singermid="+id+"&utf8=1&outCharset=utf-8&format=xml";
        String filename=url.replace("https://","")
                .replace("http:","")
                .replace(".com","")
                .replace(".cn","")
                .replace("www.","")
                .replace(".html","")
                .replace("?","/")
                .replace("=","_");

        String[] dirnames=filename.split("/");

        String dirname="";
        for(int i=0;i<dirnames.length-1;i++) {

            dirname=dirname+dirnames[i]+"\\";
        }


        String htmlfilname=dirname+dirnames[dirnames.length-1];
        File fhtml=new File(htmlfilname);
        File f=new File(dirname);
        if(!f.exists()) {
            f.mkdirs();
        }
        else if (fhtml.exists()){
            return null;
        }
        int period=1000;
        Thread.sleep(period * 1);
        String referrer="https://y.qq.com/n/yqq/singer/"+id+".html";
        doc = Jsoup.connect(url).timeout(50000)
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
                .referrer(referrer)
                .ignoreContentType(true).execute().body();


        try (FileWriter file = new FileWriter( htmlfilname+".html")) {
            file.write(doc);
        } catch (IOException exc) {
            System.out.println(exc);
        }


        return doc;
    }

    public  Document downLoad(String url,String filename)
            throws Throwable {
        Document doc;
        Thread.sleep(5 * 1000);
//        Random randomno = new Random();
//        int i=randomno.nextInt(ips.size());
//
//        Properties prop=new Properties(System.getProperties());
//        prop.put("https.proxySet","true");
//        prop.put("https.proxyHost",ips.get(i));
//        prop.put("https.proxyPort",ports.get(i));
//
//        Properties newprop=new Properties(prop);
//        System.setProperties(newprop);

        doc = Jsoup.connect(url).timeout(5000).get();
        System.out.println("Crawled:"+url+" ip:"+System.getProperty("https.proxyHost"));
        //System.out.println("Crawled:"+url);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(doc.toString());
        } catch (IOException exc) {
            System.out.println(exc);
        }

        //System.out.println(filename+""+url);

        return doc;
    }

    public  static Document downLoadProxy(String url,String ip,String port)
            throws IOException {
        Document doc;

        Properties prop=new Properties(System.getProperties());
        prop.put("https.proxySet","true");
        prop.put("https.proxyHost",ip);
        prop.put("https.proxyPort",port);

        Properties newprop=new Properties(prop);
        System.setProperties(newprop);

        doc = Jsoup.connect(url).timeout(50000).get();

        return doc;
    }

    public  static String downLoadHeader(String url)//根据url格式存储数据，对应openhtmlurl
            throws Exception {
        String  doc;


//                .header("cookie","pgv_pvi=488765440; RK=iXLNJeOxOD; ptcz=b2f0aa8041e0368a6eb43ee585a6f56d638ec37151ead255d9046a1f100d83e5; pgv_pvid=2159578344; h_uid=h584124276818862552; o_cookie=631184685; pac_uid=1_631184685; tvfe_boss_uuid=1dac8b1e169b5ae8; pgv_info=ssid=s9321190646; ts_uid=2367250816; pgv_si=s1712596992; yqq_stat=0")
//                .header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
//

        String filename=url.replace("https://","")
                .replace("http:","")
                .replace(".com","")
                .replace(".cn","")
                .replace("www.","")
                .replace(".html","")
                .replace("?","/")
                .replace("=","_");

        String[] dirnames=filename.split("/");

        String dirname="";
        for(int i=0;i<dirnames.length-1;i++) {

            dirname=dirname+dirnames[i]+"\\";
        }


        String htmlfilname=dirname+dirnames[dirnames.length-1];
        File fhtml=new File(htmlfilname);
        File f=new File(dirname);
        if(!f.exists()) {
            f.mkdirs();
        }
        else if (fhtml.exists()){
            return null;
        }
        int period=1000;
        Thread.sleep(period * 1);
        doc = Jsoup.connect(url).timeout(50000)
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
                .referrer("https://y.qq.com/n/yqq/singer/001BHDR33FZVZ0.html")
                .ignoreContentType(true).execute().body();


        try (FileWriter file = new FileWriter( htmlfilname+".html")) {
            file.write(doc);
        } catch (IOException exc) {
            System.out.println(exc);
        }


        return doc;
    }

    public  static String downLoad(String url)//根据url格式存储数据，对应openhtmlurl
            throws Exception {
        String  doc;


//                .header("cookie","pgv_pvi=488765440; RK=iXLNJeOxOD; ptcz=b2f0aa8041e0368a6eb43ee585a6f56d638ec37151ead255d9046a1f100d83e5; pgv_pvid=2159578344; h_uid=h584124276818862552; o_cookie=631184685; pac_uid=1_631184685; tvfe_boss_uuid=1dac8b1e169b5ae8; pgv_info=ssid=s9321190646; ts_uid=2367250816; pgv_si=s1712596992; yqq_stat=0")
//                .header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
//

        String filename=url.replace("https://","")
                .replace("http:","")
                .replace(".com","")
                .replace(".cn","")
                .replace("www.","")
                .replace(".html","")
                .replace("?","/")
                .replace("=","_");

        String[] dirnames=filename.split("/");

        String dirname="";
        for(int i=0;i<dirnames.length-1;i++) {

            dirname=dirname+dirnames[i]+"\\";
        }


        String htmlfilname=dirname+dirnames[dirnames.length-1];
        File fhtml=new File(htmlfilname);
        File f=new File(dirname);
        if(!f.exists()) {
            f.mkdirs();
        }
        else if (fhtml.exists()){
            return null;
        }
        int period=1000;
        Thread.sleep(period * 1);
        doc = Jsoup.connect(url).timeout(50000).ignoreContentType(true).execute().body();


        try (FileWriter file = new FileWriter( htmlfilname+".html")) {
            file.write(doc);
        } catch (IOException exc) {
            System.out.println(exc);
        }


        return doc;
    }

    public  static String createfile(String url)//根据url格式存储数据，对应openhtmlurl
            throws Exception {
        String  doc;

        String filename=url.replace("https://","")
                .replace("http:","")
                .replace(".com","")
                .replace(".cn","")
                .replace("www.","")
                .replace(".html","")
                .replace("?","/")
                .replace("=","_");

        String[] dirnames=filename.split("/");

        String dirname="";
        for(int i=0;i<dirnames.length-1;i++) {

            dirname=dirname+dirnames[i]+"\\";
        }


        String htmlfilname=dirname+dirnames[dirnames.length-1]+".html";

        File fhtml=new File(htmlfilname);
        File f=new File(dirname);
        if(!f.exists()) {
            f.mkdirs();
        }
        else if (fhtml.exists()){
            return null;
        }

        return htmlfilname;
    }

    public static void savehtml(String doc,String filname)
        throws Exception{
        System.out.println(filname);
        try (FileWriter file = new FileWriter( filname)) {
            file.write(doc);
        } catch (IOException exc) {
            System.out.println(exc);
        }

    }


}
