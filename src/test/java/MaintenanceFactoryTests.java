import org.example.monthly.MaintenanceTaskFactory;
import org.example.dao.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MaintenanceFactoryTests extends BaseDaoTests {

    private JdbcLogDao testLd;
    private JdbcTransactionDao testTd;
    private AccountDao testAd;
    private CustomerDao testCd;

    @Before
    public void setup(){
        testLd = new JdbcLogDao(dataSource);
        testTd = new JdbcTransactionDao(dataSource);
        testAd = new JdbcAccountDao(dataSource);
        testCd = new JdbcCustomerDao(dataSource);
    }

    @Test
    public void when_more_than_one_month_passed_then_accrue_interest_accrues_interest(){
        MaintenanceTaskFactory factory = new MaintenanceTaskFactory(testAd,testCd,testTd,testLd);
        factory.accrueInterest();
        int retrieved = testLd.getLastMonthlyTransactionCount();
        int expected = 2;
        Assert.assertEquals("Should only make 2 transactions",expected,retrieved);

    }
    @Test
    public void when_less_than_a_month_then_accrue_interest_does_not_accrue_interest(){

    }
    @Test
    public void when_more_than_one_month_then_monthly_fees_applied_to_low_balance_savings_accounts(){
        MaintenanceTaskFactory factory = new MaintenanceTaskFactory(testAd,testCd,testTd,testLd);
        factory.chargeMaintenanceFee();
        int retrieved = testLd.getLastMonthlyTransactionCount();
        int expected = 1;
        Assert.assertEquals("Should only make 1 transaction",expected,retrieved);

    }
    @Test
    public void when_less_than_one_month_passed_then_no_monthly_fees(){

    }
}
