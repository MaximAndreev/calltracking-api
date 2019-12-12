package ru.avtomir.calltrackingru;

import ru.avtomir.calltrackingru.beans.Project;
import ru.avtomir.calltrackingru.credential.Credential;
import ru.avtomir.calltrackingru.exceptions.RequestCalltrackingRuException;

import java.time.LocalDate;
import java.util.List;

public interface CalltrackingRu {

    /**
     * List of all {@link Project} available for account (defined by {@link Credential})
     * {@link Project} returned without {@link Project#calls}.
     *
     * @return projects without call data.
     * @throws RequestCalltrackingRuException if API returns an error in all 3 attempts.
     */
    List<Project> getAllProjects() throws RequestCalltrackingRuException;

    /**
     * Get {@link Project} with filled {@link Project#calls} for date range.
     *
     * @param id        id of project in Calltracking.ru
     * @param startDate start date.
     * @param endDate   end date.
     * @return project with call data.
     * @throws RequestCalltrackingRuException if API returns an error in all 3 attempts.
     */
    Project getProject(String id, LocalDate startDate, LocalDate endDate) throws RequestCalltrackingRuException;
}
