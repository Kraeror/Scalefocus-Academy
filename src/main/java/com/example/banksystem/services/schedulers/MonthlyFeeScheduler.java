package com.example.banksystem.services.schedulers;

import com.example.banksystem.models.entities.TransactionEntity;
import com.example.banksystem.services.CheckingAccountServiceImpl;
import com.example.banksystem.services.TransactionServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MonthlyFeeScheduler {

    private final CheckingAccountServiceImpl accountService;
    private final TransactionServiceImpl transactionService;
    private final Logger log = LogManager.getLogger(this.getClass());

    public MonthlyFeeScheduler(CheckingAccountServiceImpl accountService, TransactionServiceImpl transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }


    /**
     * Takes the monthly fee out of every account automatically every month.
     * <p>The account is taken from the service,the monthly fee corresponding to the account type is subtracted and
     * account balance is updated. A {@link TransactionEntity} of type "monthly-fee" is created in the process and
     * saved to the database.
     */
    //@Scheduled(cron = "* */4 * * * *")
    @Scheduled(cron = "@monthly")
    public void collectMonthlyFee() {
        log.info("The scheduler for monthlyFee got activated.");
        try {
            this.accountService.getAllAccounts()
                    .forEach(account -> {
                        BigDecimal tax = account.getType().getMonthlyFee();

                        account.setBalance(account.getBalance().subtract(tax));
                        log.info("The scheduler took {} from {}", tax, account.getIban());

                        accountService.saveAccount(account);
                        transactionService.savePaymentInTransaction(tax, account, "monthly fee");
                    });
        }catch (Exception e){
            throw new IllegalArgumentException("No account are present in the database!");
        }
        log.info("The scheduler for monthlyFee got deactivated.");
    }
}
