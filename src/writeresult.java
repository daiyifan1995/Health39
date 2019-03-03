import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class writeresult {
    public static void writetojson(Map<String,Object> result, String writename, Boolean append){
        try(FileWriter output = new FileWriter(writename,append)){
            JSONObject json = new JSONObject(result);
            output.write(json+"\r\n");
        }
        catch (IOException exc){
            System.out.println(exc);
        }
    }
}
