package it.medialogic.mockapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.medialogic.mockapi.exception.CustomException;
import it.medialogic.mockapi.service.CfUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/cf")
public class FiscalCodeController {

    @Autowired
    public CfUtilService cfUtilService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> getAgeAndBirth(@RequestBody String s) {

        log.info("*** POST request to get birth and age from a fiscal code ***");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        try {

            log.info("*** Parse the message ***");

            log.info("*** Get birth ***");
            String birth = cfUtilService.getBirth(s);

            log.info("*** Get age ***");
            int age = cfUtilService.getAge(s);

            node.put("birth", birth);
            node.put("age", age);

            return new ResponseEntity<>(node, HttpStatus.OK);
        }catch (CustomException e) {

            log.info("*** Error: {} ***", e.getFieldName());

            node.put("error", e.getFieldName());

            return new ResponseEntity<>(node, HttpStatus.BAD_REQUEST);
        }

    }

}
