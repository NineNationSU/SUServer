package ssau.lk;

import database.exceptions.IllegalObjectStateException;
import database.objects.Teacher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import timetable.*;

public abstract class TimeTableGrabber {

    public static String getTimeTable(String lk) throws IllegalObjectStateException {
        Document document = Jsoup.parse(lk);

        Elements div_timetable_week = document.select("div.timetable_week");

        TimeTable timeTable = new TimeTable();
        for (int i = 0; i < div_timetable_week.size(); ++i) {
            Element weekHTMLTable = div_timetable_week.get(i);
            WeekTimeTable weekTimeTable = new WeekTimeTable().setWeekNumber(i+1);
            Element div_times_block = weekHTMLTable.select("div.times_block").first();

            Elements div_time_box = div_times_block.select("div.time_box");
            if (timeTable.getTime().isEmpty()) {
                for (Element aTime_box : div_time_box) {
                    timeTable.getTime().add(aTime_box.select("span.begintime").first().text() +
                            " - " + aTime_box.select("span.endtime").first().text());
                }
            }

            Elements day_rasp_blocks = weekHTMLTable.select("div.day_rasp_block");

            for( int j = 0; j < day_rasp_blocks.size(); ++j){
                DayTimeTable dayTimeTable = new DayTimeTable().setDayOfWeek(j+1);
                String dayName = day_rasp_blocks.get(j).select("div.rasp_block_title").text();
                System.out.println(dayName);
                Elements couples = day_rasp_blocks.get(j).select("div.para_wrapper");
                int numberOfCouple = 0;
                for (Element couple : couples) {
                    /*if (!couple.text().isEmpty()) {
                        //Element lessonInfo = lesson.select("div.para_wrapper.div.rasp_info_box.div.rasp_info_box_text").first();
                        System.out.print(couple.select("div.rasp_info_discipline").text() + " / ");
                        System.out.print(couple.select("div.rasp_info_type_load").text() + " / ");
                        System.out.print(couple.select("div.rasp_info_teacher").text() + " / ");
                    }*/
                    numberOfCouple++;
                    Elements lessons = couple.select("div.discipline_info_wrapper");
                    Couple temp;
                    String discipline = null, type = null, teacherName=null;
                    StringBuilder subgroups = null;
                    if (!lessons.isEmpty()) {

                        discipline = couple.select("div.rasp_info_discipline").get(0).text();
                        type = couple.select("div.rasp_info_type_load").get(0).text();
                        try {
                            teacherName = couple.select("div.rasp_info_teacher").get(0).text();
                        }catch (IndexOutOfBoundsException e){
                            teacherName = "";
                        }
                        subgroups = new StringBuilder();
                        for (Element element : lessons.get(0).select("div.rasp_info_subgroup_box")) {
                            subgroups.append(element.select("span.rasp_info_subgroup_title").text());
                            subgroups.append(' ');
                            subgroups.append(element.select("span.rasp_info_subgroup_nums").text());
                            subgroups.append(' ');
                        }
                    }
                    if (lessons.size() == 2){
                         temp = new Couple(true);
                         dayTimeTable.getCouple().add(temp);
                         dayTimeTable.getCouple().get(numberOfCouple - 1)
                                 .setFirstSubgroup(new Lesson()
                                         .setNumber(numberOfCouple)
                                         .setDiscipline(discipline)
                                         .setType(type)
                                         .setTeacher(new Teacher().setFullName(teacherName))
                                         .setSubgroupsInfo(subgroups.toString()));
                        String discipline2 = couple.select("div.rasp_info_discipline").get(1).text();
                        String type2 = couple.select("div.rasp_info_type_load").get(1).text();
                        String teacherName2 = couple.select("div.rasp_info_teacher").get(1).text();
                        StringBuilder subgroups2 = new StringBuilder();
                        for (Element element : lessons.get(1).select("div.rasp_info_subgroup_box")){
                            subgroups2.append(element.select("span.rasp_info_subgroup_title").text());
                            subgroups2.append(' ');
                            subgroups2.append(element.select("span.rasp_info_subgroup_nums").text());
                            subgroups2.append(" ");
                        }
                        dayTimeTable.getCouple().get(numberOfCouple - 1)
                                .setSecondSubgroup(new Lesson()
                                        .setNumber(numberOfCouple)
                                        .setDiscipline(discipline2)
                                        .setType(type2)
                                        .setTeacher(new Teacher().setFullName(teacherName2))
                                        .setSubgroupsInfo(subgroups2.toString()));
                    }else if (lessons.size() == 1){
                        temp = new Couple(false);
                        dayTimeTable.getCouple().add(temp);
                        dayTimeTable.getCouple().get(numberOfCouple - 1)
                                .setAllGroup(new Lesson()
                                        .setNumber(numberOfCouple)
                                        .setDiscipline(discipline)
                                        .setType(type)
                                        .setTeacher(new Teacher().setFullName(teacherName))
                                        .setSubgroupsInfo(subgroups.toString()));
                    }else {
                        dayTimeTable.getCouple().add(null);
                    }
                    System.out.println();

                }
                weekTimeTable.getDays().add(dayTimeTable);
                System.out.println();
            }
            timeTable.getWeeks().add(weekTimeTable);

        }
        System.out.println(timeTable);
        return timeTable.toString();
    }

}
