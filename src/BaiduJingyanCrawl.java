

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class BaiduJingyanCrawl {

    int changes=0;
    int period=0;

    String[] ips={"116.209.57.9","111.177.177.145","125.123.139.64","112.85.166.21","144.255.13.11",
            "110.52.235.57", "116.209.56.199","125.123.143.200","116.209.53.237","111.177.164.239"};
    String[] port={"9999","9999","9999","9999","9999",
            "9999","9999","9999","9999","9999"};


    int crawl_times=0;

    public static void main(String[] args)
            throws IOException {

        String base="https://jingyan.baidu.com/";
        String url;

        HashMap<String,Integer> urlcrawled=new HashMap<>();

        HashSet<String> unurlcrawled=new HashSet<>();//发生了错误则放入未下载下来的


        BaiduJingyanCrawl T=new  BaiduJingyanCrawl();

        String ip=T.ips[0];
        String port=T.port[0];
        //T.changeproxy();


        //T.startcrawl(urlcrawled,unurlcrawled,ip,port);//爬虫起始点
        T.writecrawl(urlcrawled,unurlcrawled);

        BaiduJingyan B=new BaiduJingyan();

        for(String filename:B.dirList("baiduJingyanData\\user")) {
            System.out.println("—————————————————————————————————");
            System.out.println(filename);
            Document doc = OpenHtml.openHtml("baiduJingyanData\\user\\"+filename);
            url=base+"list/"+filename.replace(".html","");
            urlcrawled.put(url,1);
            T.toSet(doc, urlcrawled, unurlcrawled,ip,port);
            T.writecrawl(urlcrawled,unurlcrawled);
        }

        while(urlcrawled.containsValue(0)){
            System.out.println("`````````````````````````````````````````````````````````````````");
            //只要url中flag为0的url存在，就继续下载
            //查找是否有不通过遍历得到value为0的key
            for(Map.Entry<String,Integer> key_value : urlcrawled.entrySet()){
                if(key_value.getValue().equals(0)){
                    url =key_value.getKey();
                    String filename=url.replace(base,"")
                            .replace("?","_")
                            .replace("=","_")
                            .replace("&","_")
                            .replace(".html","");
                    urlcrawled.put(url,1);
                    Document doc = OpenHtml.openHtml(filename+".html");
                    T.toSet(doc,urlcrawled,unurlcrawled,ip,port);
                    T.writecrawl(urlcrawled,unurlcrawled);
                }

        }


        }

    }

    public void startcrawl(HashMap<String,Integer> urlcrawled,Set<String> unurlcrawled,String ip,String port)
            throws IOException{
        String base="https://jingyan.baidu.com/list/";
        BaiduJingyan b =new BaiduJingyan();
        for(int i=1;i<130;i++){
            String url=base+String.valueOf(i);
            try{
                Thread.sleep(period * 1);
                Document doc=b.downLoad(url,ip,port);

                if (doc.toString().length()>200)
                    urlcrawled.put(url,0);
                else {
                    unurlcrawled.add(url);
                    System.out.println(doc.toString());
                    System.out.println(url+"Connection Error.");
                }
            }
            catch (Throwable e){
                System.out.println(url);
                System.out.println(e);
                unurlcrawled.add(url);
            }
        }

    }

    public void toSet(Document doc,HashMap<String,Integer>  urlcrawled,HashSet<String>  unurlcrawled,String ip,String port)//把看到的url下载并存入urlcrawled
            throws IOException{
        BaiduJingyan B=new BaiduJingyan();
        Set<String> urllist=B.selecturl(doc);
        for(String url:urllist)
            //要保证该url未下载过
            if (!urlcrawled.containsKey(url)){
                try{
                    Thread.sleep(period * 1000);
                    if(url.contains("article")||url.contains("list")||url.contains("user")){
                        unurlcrawled.add(url);
                        continue;
                    }//只写下未爬取的article
                    Document newDoc=B.downLoad(url,ip,port);//若未发生错误则放入爬过的，发生了错误则放入未爬的

                    if (newDoc.toString().length()>200)
                        urlcrawled.put(url,0);
                    else {

                        unurlcrawled.add(url);
                        System.out.println(url+"Connection Error.");
                    }
                }
                catch (Throwable e){
                    unurlcrawled.add(url);
                    System.out.println(e);
                    System.out.println(url);}
            }

    }

    public void writecrawl(HashMap<String,Integer> urlcrawled, Set<String>  unurlcrawled)
            throws IOException{
        try(FileWriter output = new FileWriter("urlcrawled.txt")){
            output.write(urlcrawled.toString()+"\r\n");
        }
        catch (IOException exc){
            System.out.println(exc);
        }
        try(FileWriter output = new FileWriter("unurlcrawled_user2.txt")){
            output.write(unurlcrawled.toString()+"\r\n");
        }
        catch (IOException exc){
            System.out.println(exc);
        }
        Date date=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        System.out.println(df.format(date)+" write urlcrawled and unurlcrawled");
    }

    public void changeproxy(){
        TimerTask repeatedTask = new TimerTask() {
            public void run() {

                Properties prop=new Properties(System.getProperties());
                prop.put("https.proxySet","true");
                prop.put("https.proxyHost",ips[changes]);
                prop.put("https.proxyPort",port[changes]);

                Properties newprop=new Properties(prop);
                System.setProperties(newprop);

                if(changes<ips.length-1)
                    changes++;
                else
                    changes=0;

            }
        };
            Timer timer = new Timer("Timer");
            long delay=1000L;
            long period = 5000L;
            timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }
}
