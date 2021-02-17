package com.fetch.exercise.controller;

import com.fetch.exercise.domain.Payer;
import com.fetch.exercise.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.fetch.exercise.service.RewardsService;

import java.util.List;

@Controller
@RequestMapping(value = "/rewards", produces = MediaType.APPLICATION_JSON_VALUE)
public class RewardsController {

    private static Logger log = LoggerFactory.getLogger(RewardsController.class);

    @Autowired
    private RewardsService rewardsService;

    @RequestMapping(method = RequestMethod.GET, value = "/checkBalance")
    public @ResponseBody
    List<Payer> checkPayerBalance() {
        return rewardsService.checkBalance();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    void addPoints(@RequestBody Transaction t) {
        rewardsService.addPoints(t);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    List<Transaction> deductPoints(@RequestParam("points") int points,
                      @RequestParam("user") String userName) {
        return rewardsService.deductPoints(points, userName);
    }
}