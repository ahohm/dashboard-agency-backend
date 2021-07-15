package com.akkurad.dashboardagencybackend.service.impl;

import com.akkurad.dashboardagencybackend.dto.Filter;
import com.akkurad.dashboardagencybackend.dto.FilterContainer;
import com.akkurad.dashboardagencybackend.service.IFilterService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FilterServiceImpl implements IFilterService {


    @Autowired
    private FirebaseApp firebaseApp;
    @Autowired
    private FirebaseRemoteConfig firebaseRemoteConfig;
    @Autowired
    private Gson gson;

    public List<Filter> getFilter(String filterName) throws ExecutionException, InterruptedException {

//      ###  Obtenir le modèle de configuration à distance actuel ###
        Template template = firebaseRemoteConfig.getTemplateAsync().get();
        JSONObject object = new JSONObject(template.toJSON());

        JSONObject filterString = new JSONObject(object.getJSONObject("parameters")
                .getJSONObject(filterName)
                .getJSONObject("defaultValue")
                .getString("value"));

        JSONArray filtersArray = filterString.getJSONArray("filters");

        Filter[] filterDevices = gson.fromJson(filtersArray.toString(), Filter[].class);
        List<Filter> fdList = Arrays.asList(filterDevices);




        /* just testing begin */
//        System.out.println(((ParameterValue.Explicit)template.getParameters().get("FILTER1").getConditionalValues().get("ios")).getValue());
//        log.error(template.getParameters().get("FILTER1").getDescription());
//        template.getParameters().get("FILTER1").setDescription("new description");
//        System.out.println(template.getParameters().get("FILTER1").getConditionalValues().containsKey("ios"));
//        System.out.println(template.getParameters().get("FILTER1").getConditionalValues().get("ios"));
//
//        FirebaseRemoteConfig publishTemplate = FirebaseRemoteConfig.getInstance();
//        publishTemplate.publishTemplateAsync(template);

        /* just testing end */

        //            long  numberFilterBike =
        //                    fdList.stream()
        //                            .filter(t -> t.getType() == LockType.BIKE.name())
        //                            .count();
        return fdList;
    }

    @Override
    public void addFilter(Filter filterToAdd, Condition condition, String filterName) throws ExecutionException, InterruptedException {
        Template template = firebaseRemoteConfig.getTemplateAsync().get();

        List<Filter> filterToAddList = new ArrayList<>();
        filterToAddList.add(filterToAdd);

        FilterContainer filters = new FilterContainer();
        filters.setFilters(filterToAddList);
        String filterToAddListJson = gson.toJson(filters);


//        template.getConditions().add(new Condition("haha", "device.os == 'android' && device.country in ['us', 'uk']", TagColor.CYAN));
        template.getConditions().add(condition);

        template.getParameters().put(filterName, new Parameter().setDefaultValue(ParameterValue.of(filterToAddListJson)));



        FirebaseRemoteConfig publishTemplate = FirebaseRemoteConfig.getInstance();
        publishTemplate.publishTemplateAsync(template);
    }


    @Override
    public void removeFilter(String filterName) throws ExecutionException, InterruptedException {

        Template template = firebaseRemoteConfig.getTemplateAsync().get();
        template.getParameters().remove(filterName);
        FirebaseRemoteConfig publishTemplate = FirebaseRemoteConfig.getInstance();
        publishTemplate.publishTemplateAsync(template);
    }

    @Override
    public void updateFilter(String filterName) {
    }


    @Override
    public void saveCondition(Condition condition) throws ExecutionException, InterruptedException {
        Template template = firebaseRemoteConfig.getTemplateAsync().get();

        template.getConditions().add(condition);
        FirebaseRemoteConfig publishTemplate = FirebaseRemoteConfig.getInstance();
        publishTemplate.publishTemplateAsync(template);
    }

    @Override
    public void deleteCondition(Condition condition) throws ExecutionException, InterruptedException {
        Template template = firebaseRemoteConfig.getTemplateAsync().get();

        template.getConditions().remove(condition);
        FirebaseRemoteConfig publishTemplate = FirebaseRemoteConfig.getInstance();
        publishTemplate.publishTemplateAsync(template);
    }
}
