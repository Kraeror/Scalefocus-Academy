package com.example.banksystem.services.schedulers;

import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.services.LoanServiceImpl;
import com.example.banksystem.services.TransactionServiceImpl;
import com.example.banksystem.services.interfaces.CheckingAccountService;
import com.example.banksystem.services.interfaces.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
@Component
public class LoanScheduler {

    private final UserService userService;
    private final CheckingAccountService checkingAccountService;
    private final LoanServiceImpl loanService;
    private final TransactionServiceImpl transactionService;
    private final Logger log = LogManager.getLogger(this.getClass());

    public LoanScheduler(UserService userService, CheckingAccountService checkingAccountService,
                         LoanServiceImpl loanService, TransactionServiceImpl transactionService) {
        this.userService = userService;
        this.checkingAccountService = checkingAccountService;
        this.loanService = loanService;
        this.transactionService = transactionService;
    }

//            @Scheduled(cron = "* */1 * * * *")
    @Scheduled(cron = "0 0 8 * * *")
    public void deleteFinishedLoans() {
        log.info("The scheduler for deletion of finished loans got activated.");
        this.loanService.getAllLoansByDueDate(LocalDate.now())
                .forEach(loan -> {
                    UserEntity userEntity = loan.getAccount().getUser();
                    userEntity.setHasLoan(false);
                    userService.saveUser(userEntity);
                    log.info("The scheduler removed the loan from the user with id {}",userEntity.getId());

                    loanService.deleteLoan(loan);
                    log.info("The scheduler deleted loan with id {}",loan.getId());
                });
        log.info("The scheduler for deletion of finished loans got deactivated.");
    }

//    @Scheduled(cron = "* */1 * * * *")
    @Scheduled(cron = "0 0 9 * * *")
    public void chargeAccountsWithLoanWithMaturityDateToday() {
        log.info("The scheduler for charging accounts with maturity date today got activated.");
        this.loanService.getAllLoansByMaturityDate(LocalDate.now())
                .forEach(loan -> {
                    BigDecimal loanMonthlyPayment = loan.getMonthlyPayment();
                    loanMonthlyPayment= loanMonthlyPayment.add(loan.getLoanType().getMonthlyFee());

                    CheckingAccountEntity account = loan.getAccount();
                    account.setBalance(account.getBalance().subtract(loanMonthlyPayment));
                    checkingAccountService.saveAccount(account);
                    log.info("The scheduler subtracted monthly payment for loan with id {} from account with " +
                                    "id{} equal to {}"
                            ,loan.getId(),account.getId(),loanMonthlyPayment);

                    transactionService.savePaymentInTransaction(loanMonthlyPayment,account,"loan payment");
                    log.info("The scheduler created transaction with reason loan payment for " +
                            "loan with id{} ",loan.getId());

                    loan.setMaturityDate(loan.getMaturityDate().plusMonths(1));
                    loan.setRemainingLoanAmount(loan.getRemainingLoanAmount().subtract(loanMonthlyPayment));
                    loanService.saveLoan(loan);
                    log.info("The scheduler subtracted the remaining amount in loan with id {}" +
                                    " and added 1 mounth to the maturity date",loan.getId());


                });
        log.info("The scheduler for charging accounts with maturity date today got deactivated.");
    }

}
