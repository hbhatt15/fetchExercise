import com.fetch.exercise.domain.Payer;
import com.fetch.exercise.domain.Transaction;
import com.fetch.exercise.service.RewardsService;
import com.fetch.exercise.service.RewardsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestRewardService {

    private RewardsService rewardsService;

    @Before
    public void setUp() {
        rewardsService = new RewardsServiceImpl();      //we can mock using Mockito, but its outside the scope of how we write unit test.

        Transaction t = new Transaction();
        Payer p = new Payer("1", "Dannon", 300);
        t.setId(UUID.randomUUID().toString());
        t.setPayer(p);

        rewardsService.addPoints(t);
    }

    @Test
    public void addPoints() {
        List<Payer> output = rewardsService.checkBalance();

        List<Payer> expectedOutput = new ArrayList<>();
        expectedOutput.add(new Payer("1", "Dannon", 300));

        Assert.assertEquals(expectedOutput.size(), output.size());
    }

    @Test
    public void deductPoints() {
        List<Transaction> output = rewardsService.deductPoints(0, "user");

        Assert.assertTrue(output.size() == 0);
    }

    @Test(expected = RuntimeException.class)
    public void deductPointsException() {
        rewardsService.deductPoints(900, "user");
        rewardsService.deductPoints(100, "user");
    }

    @Test(expected = RuntimeException.class)
    public void deductPointsExceedException() {
        rewardsService.deductPoints(1000000, "user");
    }
}
