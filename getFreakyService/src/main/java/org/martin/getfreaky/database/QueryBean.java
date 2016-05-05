/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.database;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.Exercise;
import org.martin.getfreaky.dataObjects.WorkingSet;

@Stateless
public class QueryBean {

    @PersistenceContext(unitName = "freakyDB")
    private EntityManager em;

    public QueryBean() {

    }

    public void saveWorkingSet(WorkingSet workingSet) {
        em.persist(workingSet);
        
    }

    public void saveExercise(Exercise ex) {
        em.persist(ex);
    }

    public void saveDayLog(DayLog dl) {
        em.persist(dl);
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> result = em.createNamedQuery("findAllExercises").getResultList();
        return result;
    }

    public List<DayLog> getAllDaylogs() {
        List<DayLog> result = em.createNamedQuery("findAllDayLogs").getResultList();
        return result;
    }
    
    public Exercise getExercise(long id){
        return em.find(Exercise.class, id);
    }
    
    public WorkingSet getWorkingSet(long id){
        return em.find(WorkingSet.class, id);
    }

}
