package ssau.lk;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Классю предоставляющий методы для выкачивания информации о студенте
 */
public class StudentInfoGrabber {
    public static String[] getStudentName(String lk){
        Document document = Jsoup.parse(lk);
        String fullName = document.select("a.user_name").text();
        return fullName.split(" ");
    }

    /**
     * 0 - номер группы
     * 1 - факультет
     * 2 - специальность
     * 3 - бакалавр/магистр/специалист
     */
    public static ArrayList<String> getGroupInfo(String lk){
        Document document = Jsoup.parse(lk);
        String myGroup = document.select("span.group_title").text();
        System.out.println(myGroup);
        ArrayList<String> list = new ArrayList<>();
        list.add(myGroup.split(" ")[2]);
        Element left_inside = document.select("div.left_inside").first();
        Elements info = left_inside.select("span.grey_text");
        list.add(info.get(0).text());
        list.add(info.get(1).text().replace("Специальность: ", ""));
        list.add(info.get(2).text().replace("Квалификация: ", ""));
        return list;
    }
}
