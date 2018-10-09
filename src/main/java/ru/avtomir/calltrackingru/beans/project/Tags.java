package ru.avtomir.calltrackingru.beans.project;

import java.util.Arrays;
import java.util.List;

public enum Tags {
    SALE("Продажа"),
    SERVICE("Сервис"),
    PARTS("Запчасти"),
    EQUIPMENT("Доп. оборудование"),
    NON_TARGET("Нецелевой звонок"),
    OTHERS("Прочее"),
    SALE_FLEET("Продажа Юр. Лицо"),
    USED("БУ"),
    INSURANCE("Страхование"),
    UUU("УУУ"),
    NOT_ANSWERED50("Не отвеченный 50+сек"),
    MISSED_CALLS("Упущенный звонок"),
    NOT_SWITCHED_CALLS("Непереключенный звонок"),
    BAD_CONNECTION("Плохая связь");

    private String tagName;

    Tags(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public List<Tags> getMainTags() {
        return Arrays.asList(SALE, SERVICE, PARTS, EQUIPMENT, NON_TARGET, OTHERS, SALE_FLEET, USED, INSURANCE, UUU, NOT_ANSWERED50);
    }

    public List<Tags> additionalTags() {
        return Arrays.asList(MISSED_CALLS, NOT_SWITCHED_CALLS, BAD_CONNECTION);
    }

    public static Tags fromTagName(String tagName) {
        switch (tagName) {
            case "Продажа":
                return SALE;
            case "Сервис":
                return SERVICE;
            case "Запчасти":
                return PARTS;
            case "Доп. оборудование":
                return EQUIPMENT;
            case "Нецелевой звонок":
                return NON_TARGET;
            case "Прочее":
                return OTHERS;
            case "Продажа Юр. Лицо":
                return SALE_FLEET;
            case "БУ":
                return USED;
            case "Страхование":
                return INSURANCE;
            case "УУУ":
                return UUU;
            case "Не отвеченный 50+сек":
                return NOT_ANSWERED50;
            case "Упущенный звонок":
                return MISSED_CALLS;
            case "Непереключенный звонок":
                return NOT_SWITCHED_CALLS;
            case "Плохая связь":
                return BAD_CONNECTION;
            default:
                throw new IllegalArgumentException("Unknown tag");
        }
    }
}
