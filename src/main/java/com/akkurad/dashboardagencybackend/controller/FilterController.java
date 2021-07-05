package com.akkurad.dashboardagencybackend.controller;

import com.akkurad.dashboardagencybackend.dto.Filter;
import com.akkurad.dashboardagencybackend.payload.SingleFieldRequest;
import com.akkurad.dashboardagencybackend.payload.filterctrl.AddFilterRequest;
import com.akkurad.dashboardagencybackend.service.impl.FilterServiceImpl;
import com.google.firebase.remoteconfig.Condition;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/config")
@Slf4j
public class FilterController {

    @Autowired
    private FilterServiceImpl filterServiceImpl;
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Filter
     * */
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "filter", method = RequestMethod.GET)
    public ResponseEntity getFilter(@RequestBody SingleFieldRequest request){
        try{
            return new ResponseEntity(filterServiceImpl.getFilter(request.getField()), HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public ResponseEntity addFilter(@RequestBody AddFilterRequest request) throws IOException {
        try{
            AddFilterRequest filterToAdd = modelMapper.map(request, AddFilterRequest.class);

            Filter filter = filterToAdd.getFilter();
            Condition condition = new Condition(filterToAdd.getCondition().getName(), filterToAdd.getCondition().getExpression(), filterToAdd.getCondition().getTagColor());
            String name = filterToAdd.getName();
            filterServiceImpl.addFilter(filter, condition, name);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "filter", method = RequestMethod.DELETE)
    public ResponseEntity removeFilter(@RequestBody SingleFieldRequest request){
        try{
            filterServiceImpl.removeFilter(request.getField());
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "filter", method = RequestMethod.PUT)
    public ResponseEntity updateFilter(){
        try{
            filterServiceImpl.updateFilter("");
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     * Condition
     * */

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "condition",method = RequestMethod.POST)
    public ResponseEntity saveCondition(@RequestBody com.akkurad.dashboardagencybackend.payload.Condition condition){
        try{
            Condition conditionToSave = new Condition(condition.getName(), condition.getExpression(), condition.getTagColor());
            filterServiceImpl.saveCondition(conditionToSave);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "condition",method = RequestMethod.DELETE)
    public ResponseEntity deleteCondition(@RequestBody com.akkurad.dashboardagencybackend.payload.Condition condition){
        try{
            Condition conditionToRemove = new Condition(condition.getName(), condition.getExpression(), condition.getTagColor());
            filterServiceImpl.deleteCondition(conditionToRemove);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
