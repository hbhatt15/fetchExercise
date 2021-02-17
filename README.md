---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                                          Design & Thought Process

Requirements:
 - add points to user account for specific payer and date
 - deduct points from the user account using above constraints and return a list of [payer, points deducted] for each call to spend points
 - return point balance per user that would list all positive points per payer
 -  When spending points: a) deduct oldest points first b) no payer's points go negative

Clarifying Questions:
 - how is oldest points determined? If Dannon pays 300 points on 10/31 10AM, and then on 10/31 at 3PM Dannon pays -200, then the oldest date is determined based on the first time ever or is the latest time the point added?
 - does it mean then we are

Entity Relationship:
    com.fetch.exercise.domain.Transaction:
    String id
    com.fetch.exercise.domain.Payer payer
    int points
    Date transactionDate
    com.fetch.exercise.domain.User user     (purpose of this exercise we consider only one user, but in real world, there can be multiple users)

    com.fetch.exercise.domain.Payer:
    String payerID
    String payerName
    int points

    com.fetch.exercise.domain.User:
    String userID
    String userName
    int points

    com.fetch.exercise.domain.Payer can pay to multiple users, so one to many relation


Operations:
    1. Add points
    2. Deduct points
    3. Read points

more simplified version: where the index i = 0 to n goes oldest to latest timestamp
[300, 200, -200, 10000, 1000], k = 5000
[0, 200, -200, 10000, 1000] k = 4700
[0, 0, -200, 10000, 1000] k = 4500
[0, 0, 0, 10000, 1000]  k = 4700
[0, 0, 0, 5300, 1000]  k = 0

Ideally the data will be stored in the relational or NoSQL, and we do CRUD from the tables to add, deduct or get points. I will be happy to go over a call explain how this will play out if we were to expand further.
Limiting to the scope of this exercise, I am thinking to use in-memory storage like PriorityQueue to sort the transactions by date (oldest to newest). Reason: easy storage and it uses heap under the hood Time Complexity: O(nlogn)

Adding points: Adding the transaction object to Priority Queue, and to payer map, to track total points accumulated for each payer.

Algo for deducting points:
    first of all to keep track of deducted points for each payer, we need a map of payer to points
    polling the oldest transaction first
        checking if the transaction points are less than the points to be deducted, then we subtract and update the payer map, and deducted map
        if transaction points are greater than the points, then its clear that we are subtracting rest of points left from the transaction points, and update the payer map and deducted map

        any point in time if points become 0 then we have acheived our goal

    We also need to keep of totalPoints combining all the payers. ideally it will be for each User, since a user has a single balance, so we can compare with user's balance that we are deducting from, that if
    points to be deducted exceeds the user's point balance, then we throw an exception. (I limited the scope to a variable, but I just wanted to share my thoughts on what I wanted to do if this can be extended. This leads to further discussion over a phone).

Read Query:
Iterate through the payer map and return the results

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                                        How to run me
This is java springboot application, so we just need to ./gradlew bootrun
It runs on 8080

Sample requests:

PUT:
curl --location --request PUT 'http://localhost:8080/rewards' \
--header 'Content-Type: application/json' \
--data-raw '{
  "id": "d212-4c4",
  "payer": {
    "id": "1",
    "name": "DANNON",
    "points": 0
  },
  "points": 1000
}'

GET: curl --location --request GET 'http://localhost:8080/rewards/checkBalance'

POST: curl --location --request POST 'http://localhost:8080/rewards?points=5000&user=user'