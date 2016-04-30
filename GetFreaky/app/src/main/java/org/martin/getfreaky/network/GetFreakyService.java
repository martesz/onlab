package org.martin.getfreaky.network;

import org.martin.getfreaky.dataObjects.Exercise;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by martin on 2016. 04. 20..
 * This interface is used to communicate
 * with the JEE service
 */
public interface GetFreakyService {

    @GET("getFreakyService")
    Call<Exercise> getExercise();
}
