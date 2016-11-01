/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.ProgressPicture;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.network.DayLogResponse;
import org.martin.getfreaky.utils.ListUtils;

/**
 *
 * @author martin
 *
 * Provides database access to the DayLog objects.
 *
 */
@Stateless
public class DayLogDao {

    @PersistenceContext(unitName = "getfreaky")
    private EntityManager em;

    /**
     *
     * @param userId The user's id
     * @return The day logs associated with the user
     */
    public List<DayLog> getDayLogs(String userId) {
        User user = em.find(User.class, userId);
        if (user != null) {
            return user.getDayLogs();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param dayLog The day log to be inserted
     * @param userId The user's id
     * @return The result of the insert of update
     */
    public DayLogResponse insertOrUpdateDayLog(DayLog dayLog, String userId) {
        DayLog existing = em.find(DayLog.class, dayLog.getDayLogId());
        if (existing == null) {
            User user = em.find(User.class, userId);
            if (user != null) {
                user.getDayLogs().add(dayLog);
                return new DayLogResponse(DayLogResponse.ResponseMessage.DAYLOG_UPLOADED);
            } else {
                return new DayLogResponse(DayLogResponse.ResponseMessage.SOMETHING_WENT_WRONG);
            }
        } else {
            existing.setDate(dayLog.getDate());
            ListUtils.merge(existing.getWorkoutResults(), dayLog.getWorkoutResults());
            existing.getProgressPicture().setImage(dayLog.getProgressPicture().getImage());
            existing.updateBodyLog(dayLog.getBodylog());
            return new DayLogResponse(DayLogResponse.ResponseMessage.DAYLOG_UPDATED);
        }
    }

    /**
     *
     * @param userId User email
     * @param date DayLog date
     * @return If there is a DayLog of that date and user return it, if there is
     * not match return empty DayLog
     */
    public DayLog getDayLog(String userId, String date) {
        User user = em.find(User.class, userId);
        if (user != null) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            for (DayLog dl : user.getDayLogs()) {
                String actDate = fmt.format(dl.getDate());
                if (date.equals(actDate)) {
                    return dl;
                }
            }
            return new DayLog();
        } else {
            return new DayLog();
        }
    }
}
