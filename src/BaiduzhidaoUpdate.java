//
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.*;
//
//import java.net.URL;
//import java.net.URLConnection;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Set;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;//run after a given delay, or to execute periodically.
//import java.util.concurrent.TimeUnit;
//
//
//public class Main {
//
//    public static void main(String[] args)
//    throws IOException {
//        String url = "https://zhidao.baidu.com/";
//        baiduZhidao b=new baiduZhidao();
//        String dirname="baiduZhidaoData";
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        int count = 0;
//        while(count<=2880) {
//            try {
//                Thread.sleep(30 * 1000); //设置暂停的时间 30 秒
//                count ++ ;
//                System.out.println(sdf.format(new Date()) + "--循环执行第" + count + "次");
//                b.downLoad("https://zhidao.baidu.com/");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        String[] filename=b.dirList(dirname);
//        Set<String> urls=b.geturl(filename, dirname);
//        System.out.println(urls.size());
//
//    }
//
//}
//
