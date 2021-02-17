package com.fetch.exercise.service;

import com.fetch.exercise.domain.Payer;
import com.fetch.exercise.domain.Transaction;

import java.util.List;

public interface RewardsService {

    void addPoints(Transaction t);

    List<Payer> checkBalance();

    List<Transaction> deductPoints(int points, String userName);
}