package ru.avtomir.calltrackingru;

import ru.avtomir.calltrackingru.beans.project.Project;
import ru.avtomir.calltrackingru.credential.Credential;

import java.time.LocalDate;
import java.util.List;

public interface CalltrackingRu {

    /**
     * List of all {@link Project} available for account (defined by {@link Credential})
     * {@link Project} returned without {@link Project#calls}.
     */
    List<Project> getAllProjects(LocalDate startDate, LocalDate endDate);

    /**
     * Get {@link Project} with filled {@link Project#calls} for date range.
     *
     * @param isUnique - filtration of calls by "Unique by tag"({@code true}) and "Without filtration"({@code false}).
     */
    Project getProject(String id, LocalDate startDate, LocalDate endDate, boolean isUnique);
}
