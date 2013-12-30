package com.sishuok.mvc.controller;

import com.sishuok.mvc.entity.User;
import com.sishuok.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-29
 * <p>Version: 1.0
 */
@RestController
@RequestMapping("/users")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User findById(@PathVariable("id") Long id) {
        return userService.findById(1L);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> save(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
        //save user
        user.setId(1L);
        MultiValueMap headers = new HttpHeaders();
        headers.set("Location", uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUriString());
        return new ResponseEntity(user, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        //update by id
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        //delete by id
    }
}
