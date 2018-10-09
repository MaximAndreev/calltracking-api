package ru.avtomir.calltrackingru;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.avtomir.calltrackingru.beans.project.Call;
import ru.avtomir.calltrackingru.beans.project.Project;
import ru.avtomir.calltrackingru.beans.project.Tags;
import ru.avtomir.calltrackingru.credential.Credential;
import ru.avtomir.calltrackingru.exceptions.RequestCalltrackingRuException;
import ru.avtomir.calltrackingru.jsonserializers.ProjectSerializer;
import ru.avtomir.calltrackingru.request.Body;
import ru.avtomir.calltrackingru.util.Credentials;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;


public class CalltrackingRuImpl implements CalltrackingRu {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Project.class, new ProjectSerializer())
            .create();
    private static final CloseableHttpClient CLOSEABLE_HTTP_CLIENT = HttpClients.createDefault();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final URI dataUri;

    static {
        try {
            dataUri = new URIBuilder()
                    .setScheme("https")
                    .setHost("calltracking.ru")
                    .setPath("/api/get_data.php")
                    .build();
        } catch (URISyntaxException e) {
            // Unreachable statement
            throw new RuntimeException("Unreachable exception", e);
        }
    }

    private Logger logger = LoggerFactory.getLogger(CalltrackingRuImpl.class);
    private final Credential credential;

    public CalltrackingRuImpl(Credential credential) {
        this.credential = credential;
    }

    @Override
    public List<Project> getAllProjects(final LocalDate startDate,
                                        final LocalDate endDate) {
        logger.info("Get all projects for account: {}", credential.getLogin());
        Supplier<Body> bodySupplier = () ->
                new Body.Builder(credential.getToken(), "0")
                        .setDimensions("ct:project_id, ct:project_name")
                        .setMetrics("ct:phones_count")
                        .setStartDate(startDate.format(FORMATTER))
                        .setEndDate(endDate.format(FORMATTER))
                        .setMaxResults("10000")
                        .setStartIndex("0")
                        .setViewType("list")
                        .build();
        JsonElement jsonElement = postRequest(dataUri, bodySupplier);
        return GSON.fromJson(jsonElement, new TypeToken<ArrayList<Project>>() {
        }.getType());
    }

    @Override
    public Project getProject(final String id,
                              final LocalDate startDate,
                              final LocalDate endDate,
                              final boolean isUnique) {
        logger.info("get calls for id: {}", id);
        Supplier<Body> bodySupplier = () -> {
            Body.Builder builder = new Body.Builder(credential.getToken(), id)
                    .setDimensions("ct:date, ct:call_source, ct:virtual_number")
                    .setMetrics("ct:calls, ct:tagname")
                    .setStartDate(startDate.format(FORMATTER))
                    .setEndDate(endDate.format(FORMATTER))
                    .setStartIndex("0")
                    .setMaxResults("10000")
                    .setViewType("list")
                    .setUser("Maxim");
            if (isUnique) {
                builder.setScopeUnique("1");
            }
            return builder.build();
        };
        JsonElement jsonElement = postRequest(dataUri, bodySupplier);
        List<Call> calls = new ArrayList<>();
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = (JsonArray) jsonElement;
            for (JsonElement element : jsonArray) {
                if (element.isJsonObject()) {
                    calls.add(parseCall((JsonObject) element));
                }
            }
        }
        Project project = new Project();
        project.setId(Integer.valueOf(id));
        project.setCalls(calls);
        return project;
    }

    private void updateCredentials() {
        logger.info("update credentials for account: {}", credential.getLogin());
        if (!credential.isValid()) {
            URI loginUri;
            try {
                loginUri = new URIBuilder()
                        .setScheme("https")
                        .setHost("calltracking.ru")
                        .setPath("/api/login.php")
                        .setParameter("account_type", "calltracking")
                        .setParameter("login", credential.getLogin())
                        .setParameter("password", credential.getPassword())
                        .setParameter("service", "analytics")
                        .build();
            } catch (URISyntaxException e) {
                // Unreachable statement
                logger.error("reach unreachable statement");
                throw new RuntimeException("Unreachable exception", e);
            }
            JsonElement tokenJson = getRequest(loginUri);
            credential.setToken(tokenJson.getAsString());
            Credentials.save(credential);
            credential.setValid(true);
            logger.debug("get new token: {}", credential.getToken());
        }
    }

    private Call parseCall(JsonObject jsonObject) {
        JsonElement localDateElement = jsonObject.get("date");
        LocalDate localDate = null;
        try {
            if (localDateElement != null) {
                localDate = LocalDate.parse(localDateElement.getAsString(), FORMATTER);
            }
        } catch (DateTimeParseException e) {
            localDate = Call.NOT_PARSED;
        }
        JsonElement tagsElement = jsonObject.get("tagname");
        Set<Tags> tagsSet = EnumSet.noneOf(Tags.class);
        if (tagsElement != null) {
            for (String tag : tagsElement.getAsString().split(",")) {
                try {
                    tagsSet.add(Tags.fromTagName(tag));
                } catch (IllegalArgumentException e) {
                    //skip not accounted tags
                }
            }
        }
        JsonElement callSourceElement = jsonObject.get("call_source");
        String callSource = null;
        if (callSourceElement != null) {
            callSource = callSourceElement.getAsString();
        }
        JsonElement virtualNumberElement = jsonObject.get("virtual_number");
        String virtualNumber = null;
        if (virtualNumberElement != null) {
            virtualNumber = virtualNumberElement.getAsString();
        }
        return new Call(localDate, tagsSet, callSource, virtualNumber);
    }

    private JsonElement postRequest(final URI uri,
                                    final Supplier<Body> bodySupplier) {
        int attempt = 0;
        int maxAttempts = 3;
        while (true) {
            HttpPost httpPost = new HttpPost(uri);
            Body body = bodySupplier.get();
            UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(body.toFormParams(),
                    StandardCharsets.UTF_8);
            httpPost.setEntity(httpEntity);
            try {
                return execute(httpPost);
            } catch (IOException ignored) {
                if (attempt > maxAttempts) {
                    logger.warn("post-request error, uri: {}, body {}", uri, body);
                    throw new RequestCalltrackingRuException("POST-request error", uri, httpPost, ignored);
                }
                logger.info("invalid token: {}", credential.getToken());
                credential.setValid(false);
                Lock lock = credential.getCredentialStorage().getLock().writeLock();
                lock.lock();
                try {
                    logger.info("update token for account: {}", credential.getLogin());
                    updateCredentials();
                } finally {
                    lock.unlock();
                }
                attempt++;
            } catch (JsonSyntaxException | JsonIOException e) {
                throw new RequestCalltrackingRuException("POST-request error", uri, httpPost, e);
            }
        }
    }

    private JsonElement getRequest(final URI uri) {
        try {
            logger.info("send new request for token");
            return execute(new HttpGet(uri));
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            throw new RequestCalltrackingRuException("GET-request error", uri, e);
        }
    }

    private JsonElement execute(HttpUriRequest httpRequest) throws IOException {
        try (CloseableHttpResponse response = CLOSEABLE_HTTP_CLIENT.execute(httpRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                logger.warn("server response isn't 200OK: {}", statusCode);
                throw new IOException("Status isn't 200OK");
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                logger.warn("empty server response");
                throw new IOException("Server response doesn't contain body");
            }
            try (InputStreamReader isr = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8)) {
                logger.info("parse response");
                JsonObject jsonObject = GSON.fromJson(isr, JsonObject.class);
                JsonElement status = jsonObject.get("status");
                JsonElement data = jsonObject.get("data");
                if ((status != null) && "ok".equals(status.getAsString()) && (data != null)) {
                    return data;
                }
                logger.warn("api responded with error");
                throw new IOException("Server responded with error");
            }
        }
    }
}
