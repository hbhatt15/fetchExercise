package com.fetch.exercise.service;

import com.fetch.exercise.domain.Payer;
import com.fetch.exercise.domain.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

@Service
public class RewardsServiceImpl implements RewardsService {

    private static PriorityQueue<Transaction> queue = new PriorityQueue<>((t1, t2) -> t1.getTransactionDate().compareTo(t2.getTransactionDate()));
    private static Map<String, Payer> payers = new HashMap<>();
    private static int totalPoints = 0; // when we implement for multiple users, then this variable is not need. we will be keeping track per User. Since a user has a single balance, so we can compare with it.

    @Override
    public void addPoints(Transaction t) {
        t.setTransactionDate(new Date());
        String payerName = t.getPayer().getName();
        Payer p;
        if (payers.containsKey(payerName)) {
            p = payers.get(payerName);
        } else {
            p = t.getPayer();
        }
        p.setPoints(t.getPoints());
        payers.put(payerName, p);

        queue.add(t);

        totalPoints += t.getPoints();
    }

    @Override
    public List<Payer> checkBalance() {
        List<Payer> list = new ArrayList<>();
        for (Map.Entry<String, Payer> entry : payers.entrySet())
            list.add(entry.getValue());

        return list;
    }

    @Override
    public List<Transaction> deductPoints(int points, String userName) {
        //asumming its only user. logic can be added to allow multiple users.
        Map<String, Integer> pointsDeducted = new HashMap<>();
        List<Transaction> summary = new ArrayList<>();

        if (queue.isEmpty())
            throw new RuntimeException("nothing to deduct from");

        if (points <= 0)
            return summary;

        if(points > totalPoints)
            throw new RuntimeException("not enough points to be deducted from");

        while (!queue.isEmpty() && points > 0) {
            Transaction t = queue.poll();

            if (t.getPoints() == 0) continue;

            String name = t.getPayer().getName();
            Payer p = payers.get(name);

            if (t.getPoints() >= points) {
                pointsDeducted.put(name, -points);
                p.setPoints(-points);
                points = 0;
            } else if (t.getPoints() < points) {
                points = points - t.getPoints();
                p.setPoints(-t.getPoints());
                pointsDeducted.put(name, pointsDeducted.getOrDefault(name, 0) - t.getPoints());
            }

            totalPoints += pointsDeducted.get(name);
            payers.put(name, p);
        }

        for (Map.Entry<String, Integer> entry : pointsDeducted.entrySet()) {
            Transaction t = new Transaction();
            t.setId(UUID.randomUUID().toString());
            t.setPayer(payers.get(entry.getKey()));
            t.setPoints(entry.getValue());
            t.setTransactionDate(new Date());

            summary.add(t);
        }

        return summary;
    }
}