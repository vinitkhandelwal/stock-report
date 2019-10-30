import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by user on 23-04-2016.
 */
public class Test {

    public static void main(String[] args) {
//        System.out.println(GMTToLocal(new Date().toString()));
        System.out.println(method1());
        String str = "Got an infection in my eye. Pharmacist thinks something bitten me. This wouldn't have happened under Simeone. Wenger a#sarcasm #wengerin";
        removeTags(str);
    }

    private static void removeTags(String str){
        String regex = "";
        String s = str.replaceAll("#"," ");
        System.out.println(s);
    }

    private static int method1(){
        try{
            System.out.println("in try000");
            return 0;
        }
        finally {
            System.out.println("in finally00");

        }
    }

    private static  String GMTToLocal(String InputText){
        SimpleDateFormat inputFormat = new SimpleDateFormat
                ("yyyy:M:dd kk:mm", Locale.US);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat outputFormat =
                new SimpleDateFormat("kk:mm");
        // Adjust locale and zone appropriately
        Date date = null;
        try {
            date = inputFormat.parse(InputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(date);
    }
}
