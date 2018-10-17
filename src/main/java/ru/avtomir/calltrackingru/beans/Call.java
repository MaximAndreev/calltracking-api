package ru.avtomir.calltrackingru.beans;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Call {
    public static final LocalDate NOT_PARSED = LocalDate.of(3000, 1, 1);

    protected final LocalDate localDate;
    protected final Set<Tags> tags;
    protected final String callSource;
    protected final String virtualNumber;

    public Call(LocalDate localDate, Set<Tags> tags, String callSource, String virtualNumber) {
        this.localDate = localDate;
        this.tags = tags;
        this.callSource = callSource;
        this.virtualNumber = virtualNumber;
    }

    public static LocalDate getNotParsed() {
        return NOT_PARSED;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public Set<Tags> getTags() {
        return tags;
    }

    public String getCallSource() {
        return callSource;
    }

    public String getVirtualNumber() {
        return virtualNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return Objects.equals(localDate, call.localDate) &&
                Objects.equals(tags, call.tags) &&
                Objects.equals(callSource, call.callSource) &&
                Objects.equals(virtualNumber, call.virtualNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDate, tags, callSource, virtualNumber);
    }

    @Override
    public String toString() {
        return "Call{" +
                "localDate=" + localDate +
                ", tags=" + tags +
                ", callSource='" + callSource + '\'' +
                ", virtualNumber='" + virtualNumber + '\'' +
                '}';
    }
}
