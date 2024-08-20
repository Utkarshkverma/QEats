
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;

  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
        double latitude = getRestaurantsRequest.getLatitude();
    double longitude = getRestaurantsRequest.getLongitude();

    double servingRadiusInKms =  isPeakHour(currentTime)
    ? peakHoursServingRadiusInKms
    : normalHoursServingRadiusInKms;
   
    List<Restaurant> restaurants;
    restaurants = restaurantRepositoryService
        .findAllRestaurantsCloseBy(latitude, longitude, currentTime, servingRadiusInKms);


    return new GetRestaurantsResponse(restaurants);
  }

  private boolean isPeakHour(LocalTime currentTime) {
    LocalTime s1 = LocalTime.of(7, 59);
    LocalTime e1 = LocalTime.of(10, 01);

    LocalTime s2 = LocalTime.of(12, 59);
    LocalTime e2 = LocalTime.of(14, 01);

    LocalTime s3 = LocalTime.of(18, 59);
    LocalTime e3 = LocalTime.of(21, 01);

    return (currentTime.isAfter(s1) && currentTime.isBefore(e1))
            || (currentTime.isAfter(s2) && currentTime.isBefore(e2))
            || (currentTime.isAfter(s3) && currentTime.isBefore(e3));       
}

}

