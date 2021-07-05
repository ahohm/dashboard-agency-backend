package com.akkurad.dashboardagencybackend.controller;

import com.akkurad.dashboardagencybackend.dto.OwnerDto;
import com.akkurad.dashboardagencybackend.model.Owner;
import com.akkurad.dashboardagencybackend.payload.request.SaveOwnerRequest;
import com.akkurad.dashboardagencybackend.security.jwt.JwtUtils;
import com.akkurad.dashboardagencybackend.service.IOwnerService;
import com.akkurad.dashboardagencybackend.service.impl.RegisterOwnerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/owner")
@AllArgsConstructor
@Slf4j
public class OwnerController {

    private IOwnerService iOwnerService;
    private RegisterOwnerServiceImpl registerOwnerServiceImpl;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@Valid @RequestBody SaveOwnerRequest ownerDto) throws Exception {
        Owner owner = modelMapper.map(ownerDto, Owner.class);
        log.debug(String.valueOf(ownerDto));
        try {

            return new ResponseEntity(iOwnerService.save(owner), HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAll(){
        try {
            return ResponseEntity.ok(iOwnerService.findAll());
            /*List<Owner> ownerList = new ArrayList<>();
            CollectionReference owners = (CollectionReference) fireBaseInitializer.getFireBase().collection("Owner");
            ApiFuture<QuerySnapshot> querySnapshot = (ApiFuture<QuerySnapshot>) owners.getCollectionPersister();
            for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
                ownerList.add(doc.toObject(Owner.class));
            }
            return ResponseEntity.ok(ownerList);*/
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity findById(@PathVariable String id){
        try {
            return ResponseEntity.ok(iOwnerService.findById(id));
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "byusername", method = RequestMethod.GET)
    public ResponseEntity findByUsername(@PathParam(value = "username") String username){
        try {
            return ResponseEntity.ok(iOwnerService.findByUsername(username));
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable String id){
        try {
            iOwnerService.deleteById(id);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody Owner owner){
        try {
            return new ResponseEntity(iOwnerService.update(owner), HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "validateAccount/{validationToken}", method = RequestMethod.GET)
    public ResponseEntity<?> validateAccount(@PathParam("validationToken") String validationToken){
        log.warn(validationToken);
        log.error(jwtUtils.getUserNameFromJwtToken(validationToken));


        try {
            iOwnerService.validate(validationToken);
            return new ResponseEntity( HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public ResponseEntity<?> activate(){


        try {
            log.debug("dfdfdfd");
            return new ResponseEntity( HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
