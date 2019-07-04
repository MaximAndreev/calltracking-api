package ru.avtomir.calltrackingru;

import org.junit.jupiter.api.Assertions;
import ru.avtomir.calltrackingru.beans.Project;

import java.util.List;

public final class AssertUtil {

    private AssertUtil() {
    }

    public static void assertEqualsWithoutCalls(List<Project> expected, List<Project> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Project expectedProject = expected.get(i);
            Project actualProject = actual.get(i);
            org.assertj.core.api.Assertions.assertThat(actualProject)
                    .isEqualToComparingOnlyGivenFields(expectedProject, "id", "name");
        }
    }

    public static void assertEqualsWithoutCalls(Project expectedProject, Project actualProject) {
        org.assertj.core.api.Assertions.assertThat(actualProject)
                .isEqualToComparingOnlyGivenFields(expectedProject, "id", "calls");
    }
}
