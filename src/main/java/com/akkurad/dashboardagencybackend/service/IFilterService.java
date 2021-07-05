package com.akkurad.dashboardagencybackend.service;

import com.akkurad.dashboardagencybackend.dto.Filter;
import com.google.firebase.remoteconfig.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IFilterService {

    List<Filter> getFilter(String filterName) throws ExecutionException, InterruptedException;

    void addFilter(Filter filterToAdd, Condition condition, String filterName) throws ExecutionException, InterruptedException;

    void removeFilter(String filterName) throws ExecutionException, InterruptedException;

    void updateFilter(String filterName) ;

    void saveCondition(Condition condition) throws ExecutionException, InterruptedException;

    void deleteCondition(Condition condition) throws ExecutionException, InterruptedException;
}
