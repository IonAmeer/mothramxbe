package com.ionidea.mothramxbe.security.controller;

import com.ionidea.mothramxbe.security.dto.AuthorityDto;
import com.ionidea.mothramxbe.security.constants.AppConstants;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authorities")
@CrossOrigin
public class AuthorityController {

    @GetMapping
    public List<AuthorityDto> getAllAuthorities() {

        AtomicLong counter = new AtomicLong(1);

        return AppConstants.DEFAULT_AUTHORITIES.stream()
                .map(name -> new AuthorityDto(counter.getAndIncrement(), name))
                .collect(Collectors.toList());
    }

}
