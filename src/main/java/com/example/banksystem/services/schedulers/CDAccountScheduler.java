package com.example.banksystem.services.schedulers;

import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.CDAccountEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.services.interfaces.CDAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CDAccountScheduler {
    private final CDAccountService cdAccountService;

    private final Logger logger = LogManager.getLogger(this.getClass());

    public CDAccountScheduler(CDAccountService cdAccountService) {
        this.cdAccountService = cdAccountService;
    }


    /**
     * Checks the expiration date of every certification of deposit account automatically every day.
     * If there are {@link CDAccountEntity} expiring, the account is automatically transformed
     * into {@link CheckingAccountEntity} with the expected balance from the {@link CDAccountEntity} and saved
     * to the database.
     */

    //@Scheduled(cron = "* */1 * * * *")
    @Scheduled(cron = "@daily")
    public void checkExpPeriodAndTransformToChecking () {
        logger.info("The scheduler for checking expiration dates on CD Accounts got activated.");
        try {
            cdAccountService.getAllCDAccountsByDate(LocalDate.now())
                    .forEach(account -> {
                        cdAccountService.transformCDAccountToCheckingAccount(account, account.getOutcomeAmount());
                        logger.info("Certification of deposit account with IBAN: {} expired and transformed to " +
                                "Checking account with the same IBAN", account.getIban());
                    });
        } catch (NoRecordsOfEntityInTheDatabase e) {
            logger.info(e.getMessage());
        }
        logger.info("The scheduler for checking expiration dates on CD Accounts got deactivated.");
    }
}
