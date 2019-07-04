package ru.avtomir.calltrackingru;

import ru.avtomir.calltrackingru.beans.Call;
import ru.avtomir.calltrackingru.beans.Project;
import ru.avtomir.calltrackingru.beans.Tags;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public final class MockData {

    public static final Project PROJECT_1 = new Project(4955, "Проект #1");
    public static final Project PROJECT_2 = new Project(1223, "Проект #2");
    public static final Project PROJECT_3 = new Project(5026, "Проект #3");
    public static final String NEW_TOKEN = "token-token";

    public static final String VALID_ALL_PROJECTS = "" +
            "{'status':'ok'," +
            "'data':" +
            "[" +
            "{'project_id':" + PROJECT_1.getId() + ",'project_name':'" + PROJECT_1.getName() + "','phones_count':'(not set)'}," +
            "{'project_id':" + PROJECT_2.getId() + ",'project_name':'" + PROJECT_2.getName() + "','phones_count':'(not set)'}," +
            "{'project_id':" + PROJECT_3.getId() + ",'project_name':'" + PROJECT_3.getName() + "','phones_count':'(not set)'}" +
            "]," +
            "'error_code':'0'," +
            "'error_text':''}";

    public static final String VALID_NEW_TOKEN = "" +
            "{'status':'ok'," +
            "'data':'" + NEW_TOKEN + "'," +
            "'error_code':'0'," +
            "'error_text':''}";

    static {
        List<Call> calls = new ArrayList<>();
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.SALE)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.SERVICE)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.PARTS)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.EQUIPMENT)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.NON_TARGET, Tags.OTHERS)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.SALE_FLEET, Tags.USED)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.USED)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.INSURANCE)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.NOT_ANSWERED50)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.MISSED_CALLS)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.NOT_SWITCHED_CALLS)),
                "comagic",
                "74959999999"));
        calls.add(new Call(
                LocalDate.of(2019, 5, 8),
                new HashSet<>(Arrays.asList(Tags.BAD_CONNECTION, Tags.SALE)),
                "comagic",
                "74959999999"));
        PROJECT_1.setCalls(calls);
    }

    private MockData() {
    }

    public static String getJsonFromCalls(List<Call> calls) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        List<String> callData = new ArrayList<>();
        calls.forEach(call -> {
            callData.add(
                    "{'date':'" + call.getLocalDate().format(formatter) +
                            "','call_source':'" + call.getCallSource() +
                            "','virtual_number':'" + call.getVirtualNumber() +
                            "','calls':1,'tagname':'" + call.getTags().stream().map(Tags::getTagName).collect(Collectors.joining(",")) + "'}"
            );
        });
        return "{'status':'ok'," +
                "'data':[" + String.join(",", callData) + "]," +
                "'error_code':'0'," +
                "'error_text':''}";
    }
}
