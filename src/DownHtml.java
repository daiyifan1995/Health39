

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import javax.print.Doc;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

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
            throws IOException{
        String url="http://www.baike.com/wiki/%E9%99%88%E8%8B%B1%E4%BF%8A";
        System.out.println(URLDecoder.decode(url,"utf8"));
        //DownHtml.downLoad(url+URLEncoder.encode("中国共产党重大事项请示报告条例","utf8")+"/23270043");
        //"https://baike.baidu.com/item/%E4%B8%AD%E5%9B%BD%E5%85%B1%E4%BA%A7%E5%85%9A%E9%87%8D%E5%A4%A7%E4%BA%8B%E9%A1%B9%E8%AF%B7%E7%A4%BA%E6%8A%A5%E5%91%8A%E6%9D%A1%E4%BE%8B/23270043

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

        public  static Document downLoadTest(String url,String ip,String port)
            throws IOException {
        Document doc;

        Properties prop=new Properties(System.getProperties());
        prop.put("https.proxySet","true");
        prop.put("https.proxyHost",ip);
        prop.put("https.proxyPort",port);

        Properties newprop=new Properties(prop);
        System.setProperties(newprop);

        doc = Jsoup.connect(url).timeout(50000).get();

        //System.out.println(filename+""+url);

        return doc;
    }

    public  static Document downLoad(String url)//根据url格式存储数据，对应openhtmlurl
            throws IOException {
        Document doc;
        System.out.println(url);

        doc = Jsoup.connect(url).timeout(50000).get();

        String filename=url.replace("https://","")
                .replace("http:","")
                .replace(".com","")
                .replace(".cn","")
                .replace("www.","")
                .replace(".html","");

        String[] dirnames=filename.split("/");
        System.out.println(filename);

        String dirname="";
        for(int i=0;i<dirnames.length-1;i++) {

            dirname=dirname+dirnames[i]+"\\";
        }

        File f=new File(dirname);
        if(!f.exists())
            f.mkdirs();


        try (FileWriter file = new FileWriter(dirname+dirnames[dirnames.length-1]+".html")) {
            file.write(doc.toString());
        } catch (IOException exc) {
            System.out.println(exc);
        }

        //System.out.println(filename+""+url);

        return doc;
    }

//    public void get_Proxy(String url)
//            throws IOException{
//        proxy pro=new proxy();
//
//        int j =0;
//        for(HashMap<String,String> ip:pro.Ips){
//
//            if(ip.get("ip")!=null&&ip.get("port")!=null){
//                ips.add(ip.get("ip"));
//                ports.add(ip.get("port"));
//
//            }
//        }
//        System.out.println(ips);
//        Random randomno = new Random();
//        int i=randomno.nextInt(ips.size());
//        System.out.println(ips.get(i));
//
//    }

}
