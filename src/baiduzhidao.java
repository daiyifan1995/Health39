
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.SimpleDateFormat;

public class baiduZhidao {

    String cookie = "BIDUPSID=E9E45583BD76E5A9AB847071F5E1CCE2; PSTM=1539700175; BAIDUID=66258A5EAAC75CDC725178D0565C0696:FG=1; isMetisClick=click; PREV_DAILY_PERIOD=2001; BDRCVFR[r3UZWEoDlR0]=9xWipS8B-FspA7EnHc1QhPEUf; delPer=0; PSINO=2; H_PS_PSSID=; Hm_lvt_6859ce5aaf00fb00387e6434e4fcc925=1550656531,1550657126,1550657540,1550711781; IKUT=1344; dis_qids=1712551351%2C1336135360%2C1714931926%2C1714932669%2C1712335140%2C1717852948%2C1706130584%2C1714932167%2C1707561989; ZD_ENTRY=empty; Hm_lpvt_6859ce5aaf00fb00387e6434e4fcc925=1550711916";
    String cookie2="PSTM=1509021464; BIDUPSID=5F2386C9715E1A6A2AE04D675A4388C2; BDUSS=EtQWVMxRW9IWW84UFplSDhPOEJHdHd4VGhMQzZrc0QwalpnTDE3R2RQT2FNb1ZhQVFBQUFBJCQAAAAAAAAAAAEAAADG3i45AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJqlXVqapV1ab; MCITY=-%3A; BAIDUID=633B5CABB98D9CF7708358CD9C540ED4:FG=1; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; pgv_pvi=58758144; delPer=0; PSINO=3; BDRCVFR[QxxZVyx49rf]=I67x6TjHwwYf0; H_PS_PSSID=; IKUT=1056; ZD_ENTRY=baidu; Hm_lvt_6859ce5aaf00fb00387e6434e4fcc925=1550567782,1550628141,1550715237,1550719589; dis_qids=1712551351%2C1717906446%2C1714931926%2C1714932669%2C756337952%2C1336135360%2C1712335140%2C1706130584%2C1717852948; Hm_lpvt_6859ce5aaf00fb00387e6434e4fcc925=1550719626; PMS_JT=%28%7B%22s%22%3A1550719719884%2C%22r%22%3A%22https%3A//zhidao.baidu.com/list%3Fcid%3D101100%26tag%3DP2P%26tagType%3Dinit%22%7D%29";

    public String downLoad(String url)
            throws IOException {

        Document doc = Jsoup.connect(url)
                .header("cookie", cookie2)
                .timeout(5000)
                .post();

        Date current = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String filename = df.format(current);

        try (FileWriter file = new FileWriter("baiduZhidaoData\\" + filename + ".html")) {
            file.write(doc.toString());
        } catch (IOException exc) {
            System.out.println(exc);
        }

        return filename;
    }

    public String[] dirList(String dirname)
            throws IOException {
        File dir = new File(dirname);
        String[] children = dir.list();
        if (children == null) {
            System.out.println("该目录不存在");
        } else {
            for (int i = 0; i < children.length; i++) {
                String filename = children[i];
            }
        }
        return children;
    }

    public Set<String> geturl(String[] filename, String dirname)
            throws IOException {
        Set<String> urls = new LinkedHashSet<String>();
        for (int i = 0; i < filename.length; i++) {
            try {
                File input = new File(dirname + "\\" + filename[i]);//使用jsoup的方法直接打开html文件
                Document doc = Jsoup.parse(input, "UTF-8");
                Elements xs=doc.select("a[class='question-link']");
                for(Element x:xs){
                    String url=x.attr("href");
                    urls.add(url);
                }
            } catch (IOException exc) {
                System.out.println(exc);
            }
        }
        return urls;

    }



    public void saveUrl(Set<String> urls)
        throws IOException{
        Date current = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        String filename = df.format(current);
        File writename = new File(filename+".txt");
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        for(String url:urls)
            out.write(url+"\r\n"); // \r\n即为换行

        out.flush(); // 把缓存区内容压入文件
        out.close(); // 最后记得关闭文件


    }
}


