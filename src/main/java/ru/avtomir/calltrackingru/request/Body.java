package ru.avtomir.calltrackingru.request;

import com.google.gson.JsonObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Body {

    private final String auth;
    private final String project;
    private final String dimensions;
    private final String metrics;
    private final String startDate;
    private final String endDate;
    private final String startIndex;
    private final String maxResults;
    private final String viewType;
    private final String scopeUnique;
    private final String user;

    private Body(Builder builder) {
        this.auth = builder.auth;
        this.project = builder.project;
        this.dimensions = builder.dimensions;
        this.metrics = builder.metrics;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.startIndex = builder.startIndex;
        this.maxResults = builder.maxResults;
        this.viewType = builder.viewType;
        this.scopeUnique = builder.scopeUnique;
        this.user = builder.user;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (auth != null) jsonObject.addProperty("auth", auth);
        if (project != null) jsonObject.addProperty("beans", project);
        if (dimensions != null) jsonObject.addProperty("dimensions", dimensions);
        if (metrics != null) jsonObject.addProperty("metrics", metrics);
        if (startDate != null) jsonObject.addProperty("start-date", startDate);
        if (endDate != null) jsonObject.addProperty("end-date", endDate);
        if (startIndex != null) jsonObject.addProperty("start-index", startIndex);
        if (maxResults != null) jsonObject.addProperty("max-results", maxResults);
        if (viewType != null) jsonObject.addProperty("view-type", viewType);
        if (scopeUnique != null) jsonObject.addProperty("scope-unique", scopeUnique);
        if (user != null) jsonObject.addProperty("user", user);
        return jsonObject;
    }

    public List<NameValuePair> toFormParams() {
        List<NameValuePair> formParams = new ArrayList<>();
        if (auth != null) formParams.add(new BasicNameValuePair("auth", auth));
        if (project != null) formParams.add(new BasicNameValuePair("project", project));
        if (dimensions != null) formParams.add(new BasicNameValuePair("dimensions", dimensions));
        if (metrics != null) formParams.add(new BasicNameValuePair("metrics", metrics));
        if (startDate != null) formParams.add(new BasicNameValuePair("start-date", startDate));
        if (endDate != null) formParams.add(new BasicNameValuePair("end-date", endDate));
        if (startIndex != null) formParams.add(new BasicNameValuePair("start-index", startIndex));
        if (maxResults != null) formParams.add(new BasicNameValuePair("max-results", maxResults));
        if (viewType != null) formParams.add(new BasicNameValuePair("view-type", viewType));
        if (scopeUnique != null) formParams.add(new BasicNameValuePair("scope-unique", scopeUnique));
        if (user != null) formParams.add(new BasicNameValuePair("user", user));
        return formParams;
    }

    public static class Builder {
        private final String auth;
        private final String project;
        private String dimensions;
        private String metrics;
        private String startDate;
        private String endDate;
        private String startIndex;
        private String maxResults;
        private String viewType;
        private String scopeUnique;
        private String user;

        public Builder(String auth, String project) {
            this.auth = auth;
            this.project = project;
        }

        public Builder setDimensions(String dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder setMetrics(String metrics) {
            this.metrics = metrics;
            return this;
        }

        public Builder setStartDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setStartIndex(String startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public Builder setMaxResults(String maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder setViewType(String viewType) {
            this.viewType = viewType;
            return this;
        }

        public Builder setScopeUnique(String scopeUnique) {
            this.scopeUnique = scopeUnique;
            return this;
        }

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public Body build() {
            return new Body(this);
        }
    }

    @Override
    public String toString() {
        return "Body{" +
                "auth='" + auth + '\'' +
                ", project='" + project + '\'' +
                ", dimensions='" + dimensions + '\'' +
                ", metrics='" + metrics + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", startIndex='" + startIndex + '\'' +
                ", maxResults='" + maxResults + '\'' +
                ", viewType='" + viewType + '\'' +
                ", scopeUnique='" + scopeUnique + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
