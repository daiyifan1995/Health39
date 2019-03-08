//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.util.ArrayList;
//
//public class download {
//    public static void main(String[] args){
//        String filename="unurlcrawled_user.txt";
//        ArrayList<String> arrayList = new ArrayList<>();
//        try(BufferedReader br=new BufferedReader(new FileReader(filename))){
//            String[] urls;
//            String url;
//            while((url=br.readLine())!=null){
//               arrayList.add(url);
//            }
//            url=arrayList.get(0).replaceAll("\\[|]","");
//            System.out.println(url);
//            urls=url.split(",");
//            baiduJingyan D=new baiduJingyan();
//            for(int x=0;x<urls.length;x++){
//                System.out.println(x);
//                if(urls[x].contains("user")||urls[x].contains("list")){
//                    try{
//
//                        D.downLoad(urls[x],"12","123");
//
//                    }
//                    catch (Throwable e2){
//                        System.out.println(e2);
//
//                    }
//                }
//                else {
//                    //System.out.println(urls[x]);
//                }
//            }
//        }
//        catch (Throwable exc){
//            System.out.println(1);
//
//        }
//    }
//}