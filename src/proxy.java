import jdk.internal.dynalink.beans.StaticClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.rmi.runtime.NewThreadAction;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class proxy {

    int changes=0;
    Set< HashMap<String,String>> Ips=new HashSet<>();
    ArrayList<String> ips=new ArrayList<>();
    ArrayList<String> ports=new ArrayList<>();

    proxy()
        throws IOException {
        String url = "https://www.xicidaili.com/";
        Document doc = OpenHtml.openHtmlurl(url);

        this.Ips = selectproxy(doc);
        for(HashMap<String,String> ip:Ips){

            if(ip.get("ip")!=null&&ip.get("port")!=null&&ip.get("ip")!="27.155.83.182"){
                ips.add(ip.get("ip"));
                ports.add(ip.get("port"));

            }
        }
    }


    public static void main(String[] args)
            throws IOException {
        proxy x= new proxy();
    }


    public static Set< HashMap<String,String>> selectproxy(Document doc){

        Set< HashMap<String,String>> Ips=new HashSet<>();

        Elements cols=doc.selectFirst("table[id='ip_list']").select("tr");

        Pattern ipPattern=Pattern.compile("[0-9]*\\.[0-9]*\\.[0-9]*\\.[0-9]*");
        Pattern portPattern=Pattern.compile("[0-9]*");
        Pattern httpPattern=Pattern.compile("[A-Z]*");

        try{
            for(Element col :cols) {
                if(col.toString().contains("高匿")){
                    Elements rows=col.select("td");

                    HashMap<String,String> ip=new HashMap<>();
                    for(Element row:rows){


                        String x=row.text();

                        if(ipPattern.matcher(x).matches()){
                            ip.put("ip",x);

                        }
                        if(portPattern.matcher(x).matches()) {
                            ip.put("port", x);
                        }
                        if(httpPattern.matcher(x).matches()){
                            ip.put("http/https",x);
                        }

                        if(ip.containsValue("HTTP")||ip.containsValue("HTTPS"))
                            Ips.add(ip);
                    }
                }

            }

        }
        catch(Throwable e){
            System.out.println(e);
        }
        return Ips;

    }

    public static Set< HashMap<String,String>> testIp(Set< HashMap<String,String>> Ips)
            throws IOException{
        Set< HashMap<String,String>> Ip=new HashSet<>();

        for(HashMap<String,String> map:Ips){
            String ip=map.get("ip");
            String port=map.get("port");
            System.out.println(map);
            try{
                Document testDoc = DownHtml.downLoadTest("https://www.xicidaili.com/",ip,port);
                if(testDoc.toString().length()>1000)
                {
                    Ip.add(map);

                }            }
            catch(Throwable e){
                System.out.println(ip);
            }


        }


        return Ip;
    }

    public void changeproxy(){
        TimerTask repeatedTask = new TimerTask() {

            public void run() {

                Properties prop=new Properties(System.getProperties());
                prop.put("https.proxySet","true");
                prop.put("https.proxyHost",ips.get(changes));
                prop.put("https.proxyPort",ports.get(changes));

                Properties newprop=new Properties(prop);
                System.setProperties(newprop);

                if(changes<ips.size()-1)
                    changes++;
                else
                    changes=0;

            }
        };
        Timer timer = new Timer("Timer");
        long delay=1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

}