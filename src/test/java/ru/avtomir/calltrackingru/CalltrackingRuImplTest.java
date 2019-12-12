package ru.avtomir.calltrackingru;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.avtomir.calltrackingru.beans.Project;
import ru.avtomir.calltrackingru.credential.Credential;
import ru.avtomir.calltrackingru.exceptions.RequestCalltrackingRuException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalltrackingRuImplTest {

    @Mock
    CloseableHttpClient httpClient;

    @Mock
    Credential credential;

    @InjectMocks
    CalltrackingRuImpl calltrackingRu;

    @Test
    void getAllProjects() throws IOException {
        // given
        when(credential.getToken()).thenReturn("token");

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(httpClient.execute(ArgumentMatchers.any())).thenReturn(response);

        StatusLine statusLine = mock(StatusLine.class);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);

        HttpEntity entity = mock(HttpEntity.class);
        when(response.getEntity()).thenReturn(entity);

        String apiResponseBody = MockData.VALID_ALL_PROJECTS;
        InputStream is = new ByteArrayInputStream(apiResponseBody.getBytes());
        when(entity.getContent()).thenReturn(is);
        // when
        List<Project> actual = calltrackingRu.getAllProjects();

        // then
        verify(credential).getToken();
        verify(httpClient).execute(ArgumentMatchers.any());
        verify(response).getStatusLine();
        verify(statusLine).getStatusCode();
        verify(response).getEntity();
        verify(entity).getContent();
        List<Project> expected = new ArrayList<>();
        expected.add(MockData.PROJECT_1);
        expected.add(MockData.PROJECT_2);
        expected.add(MockData.PROJECT_3);
        AssertUtil.assertEqualsWithoutCalls(expected, actual);
    }

    @Test
    void statusIsNot200() throws IOException, URISyntaxException {
        // given
        when(credential.getToken()).thenReturn("token");
        when(credential.getLogin()).thenReturn("api-login");

        // A bad response
        CloseableHttpResponse responseData = mock(CloseableHttpResponse.class);
        doReturn(responseData).when(httpClient).execute(ArgumentMatchers.any(HttpPost.class));
        StatusLine statusLineData = mock(StatusLine.class);
        when(responseData.getStatusLine()).thenReturn(statusLineData);
        when(statusLineData.getStatusCode()).thenReturn(404);

        // A new token
        CloseableHttpResponse responseToken = mock(CloseableHttpResponse.class);
        doReturn(responseToken).when(httpClient).execute(ArgumentMatchers.any(HttpGet.class));
        HttpEntity httpEntityToken = mock(HttpEntity.class);
        StatusLine statusLineToken = mock(StatusLine.class);
        when(responseToken.getStatusLine()).thenReturn(statusLineToken);
        when(statusLineToken.getStatusCode()).thenReturn(200);
        when(responseToken.getEntity()).thenReturn(httpEntityToken);
        Supplier<InputStream> inputStreamSupplier = () -> {
            String apiResponseBodyToken = MockData.VALID_NEW_TOKEN;
            return new ByteArrayInputStream(apiResponseBodyToken.getBytes());
        };
        when(httpEntityToken.getContent()).thenReturn(
                inputStreamSupplier.get(),
                inputStreamSupplier.get(),
                inputStreamSupplier.get(),
                inputStreamSupplier.get());

        // when + then
        Assertions.assertThrows(RequestCalltrackingRuException.class, () -> calltrackingRu.getAllProjects());

        // then
        int maxAttempts = 3;
        verify(httpClient, times(maxAttempts)).execute(ArgumentMatchers.any(HttpGet.class));
        verify(httpClient, times(maxAttempts + 1)).execute(ArgumentMatchers.any(HttpPost.class));
    }

    @Test
    void responseBodyNull() throws IOException, URISyntaxException {
        // given
        when(credential.getToken()).thenReturn("token");
        when(credential.getLogin()).thenReturn("api-login");

        // A bad response
        CloseableHttpResponse responseData = mock(CloseableHttpResponse.class);
        doReturn(responseData).when(httpClient).execute(ArgumentMatchers.any(HttpPost.class));
        StatusLine statusLineData = mock(StatusLine.class);
        when(responseData.getStatusLine()).thenReturn(statusLineData);
        when(statusLineData.getStatusCode()).thenReturn(200);
        HttpEntity entityData = mock(HttpEntity.class);
        when(responseData.getEntity()).thenReturn(entityData);
        String apiResponseBody = "not valid json";
        InputStream is = new ByteArrayInputStream(apiResponseBody.getBytes());
        when(entityData.getContent()).thenReturn(is);

        // when + then
        Assertions.assertThrows(RequestCalltrackingRuException.class, () -> calltrackingRu.getAllProjects());
    }

    @Test
    void responseBodyNotExpectedORValidJson() throws IOException, URISyntaxException {
        // given
        when(credential.getToken()).thenReturn("token");
        when(credential.getLogin()).thenReturn("api-login");

        // A bad response
        CloseableHttpResponse responseData = mock(CloseableHttpResponse.class);
        doReturn(responseData).when(httpClient).execute(ArgumentMatchers.any(HttpPost.class));
        StatusLine statusLineData = mock(StatusLine.class);
        when(responseData.getStatusLine()).thenReturn(statusLineData);
        when(statusLineData.getStatusCode()).thenReturn(200);
        when(responseData.getEntity()).thenReturn(null);

        // A new token
        CloseableHttpResponse responseToken = mock(CloseableHttpResponse.class);
        doReturn(responseToken).when(httpClient).execute(ArgumentMatchers.any(HttpGet.class));
        HttpEntity httpEntityToken = mock(HttpEntity.class);
        StatusLine statusLineToken = mock(StatusLine.class);
        when(responseToken.getStatusLine()).thenReturn(statusLineToken);
        when(statusLineToken.getStatusCode()).thenReturn(200);
        when(responseToken.getEntity()).thenReturn(httpEntityToken);
        Supplier<InputStream> inputStreamSupplier = () -> {
            String apiResponseBodyToken = MockData.VALID_NEW_TOKEN;
            return new ByteArrayInputStream(apiResponseBodyToken.getBytes());
        };
        when(httpEntityToken.getContent()).thenReturn(
                inputStreamSupplier.get(),
                inputStreamSupplier.get(),
                inputStreamSupplier.get(),
                inputStreamSupplier.get());

        // when + then
        Assertions.assertThrows(RequestCalltrackingRuException.class, () -> calltrackingRu.getAllProjects());

        // then
        int maxAttempts = 3;
        verify(httpClient, times(maxAttempts)).execute(ArgumentMatchers.any(HttpGet.class));
        verify(httpClient, times(maxAttempts + 1)).execute(ArgumentMatchers.any(HttpPost.class));
    }

    @Test
    void getProject() throws IOException {
        // given
        when(credential.getToken()).thenReturn("token");

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(httpClient.execute(ArgumentMatchers.any())).thenReturn(response);

        StatusLine statusLine = mock(StatusLine.class);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);

        HttpEntity entity = mock(HttpEntity.class);
        when(response.getEntity()).thenReturn(entity);

        String apiResponseBody = MockData.getJsonFromCalls(MockData.PROJECT_1.getCalls());
        InputStream is = new ByteArrayInputStream(apiResponseBody.getBytes());
        when(entity.getContent()).thenReturn(is);

        // when
        Project actual = calltrackingRu.getProject(
                String.valueOf(MockData.PROJECT_1.getId()),
                LocalDate.now(),
                LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));

        // then
        verify(credential).getToken();
        verify(httpClient).execute(ArgumentMatchers.any());
        verify(response).getStatusLine();
        verify(statusLine).getStatusCode();
        verify(response).getEntity();
        verify(entity).getContent();
        AssertUtil.assertEqualsWithoutCalls(MockData.PROJECT_1, actual);
    }
}
