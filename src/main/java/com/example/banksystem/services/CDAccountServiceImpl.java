package com.example.banksystem.services;

import com.example.banksystem.exceptions.AccountNotBelongToUserException;
import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.exceptions.ResourceNotBelongingToUser;
import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.entities.CDAccountEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.requsts.CDAccountCreationRequest;
import com.example.banksystem.models.requsts.TransactionCreationRequest;
import com.example.banksystem.models.responses.CDAccountResponse;
import com.example.banksystem.models.responses.TransactionResponse;
import com.example.banksystem.repositories.CDAccountRepository;
import com.example.banksystem.services.interfaces.AccountTypeService;
import com.example.banksystem.services.interfaces.CDAccountService;
import com.example.banksystem.services.interfaces.TransactionService;
import com.example.banksystem.services.interfaces.UserService;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Service class for {@link CDAccountEntity}. Has methods for creation of
 * Certification of Deposit account and manipulating it. Also, can save and retrieve
 * {@link CDAccountEntity} from the database.
 */
@Service
public class CDAccountServiceImpl implements CDAccountService {
    private final CDAccountRepository cdAccountRepository;
    private final AccountTypeService accountTypeService;
    private final UserService userService;
    private final CheckingAccountServiceImpl checkingAccountService;
    private final ModelMapper modelMapper;
    private final TransactionService transactionService;

    public CDAccountServiceImpl(CDAccountRepository cdAccountRepository, AccountTypeService accountTypeService,
                                UserService userService, CheckingAccountServiceImpl checkingAccountService,
                                ModelMapper modelMapper, TransactionService transactionService) {
        this.cdAccountRepository = cdAccountRepository;
        this.accountTypeService = accountTypeService;
        this.userService = userService;
        this.checkingAccountService = checkingAccountService;
        this.modelMapper = modelMapper;
        this.transactionService = transactionService;
    }

    /**
     * {@inheritDoc}
     * @param acId the ID of the sought Certification of Deposit account.
     * @param userId the ID of the sought of the user trying to see the recourse.
     * @return {@link CDAccountEntity} with the corresponding ID.
     * @throws AccountNotBelongToUserException if the resource doesn't belong to the logged-in user or he is not admin.
     * @throws EntityNotFoundException if there is no account with this ID.
     */

    @Override
    public CDAccountResponse getCDAccountEntityByID(Long acId,Long userId) {
        CDAccountEntity account = cdAccountRepository.findById(acId)
                                .orElseThrow( () -> new EntityNotFoundException("Certification of Deposit Account"));
        if(isAdmin(userId) && account.getUser().getId() != userId){
            throw new ResourceNotBelongingToUser("Certification of Deposit Account");
        }
        return modelMapper.map(account, CDAccountResponse.class)
                .setFullName(account.getUser().getFullName());
    }

    /**
     * {@inheritDoc}
     * @return {@link List} of {@link CDAccountEntity}.
     * @throws NoRecordsOfEntityInTheDatabase when no entities are retrieved from the database.
     */
    @Override
    public List<CDAccountResponse> getAllCDAccountEntities() {
        List<CDAccountEntity> allCdAccounts = cdAccountRepository.findAll();
        if(allCdAccounts.isEmpty())
            throw new NoRecordsOfEntityInTheDatabase("Certificate of Deposit Account");

        return allCdAccounts.stream().map(account ->
                modelMapper.map(account, CDAccountResponse.class)
                        .setFullName(account.getUser().getFullName())).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * @param iban the Iban of the sought Certification of Deposit account.
     * @return {@link CDAccountEntity} with the corresponding Iban.
     * @throws EntityNotFoundException if there is no account with this Iban.
     */
    @Override
    public CDAccountResponse getCDAccountByIban(String iban) {
        CDAccountEntity account = cdAccountRepository.findByIban(iban)
                                .orElseThrow( (()-> new EntityNotFoundException("Certification of Deposit Account")));
        return modelMapper.map(account, CDAccountResponse.class)
                .setFullName(account.getUser().getFullName());
    }

    /**
     * {@inheritDoc}
     * @param request an entity containing the data for the new account.
     * @param id the username for the user to whom the account will be assigned.
     * @return a {@link CDAccountResponse} with the data of the saved account.
     */
    @Override
    public CDAccountResponse createCdAccount(CDAccountCreationRequest request, Long id) {
        AccountTypeEntity accountType = accountTypeService.findAccountTypeByType("Certification of deposit");
        UserEntity user = userService.getUserEntityById(id);
        BigDecimal amount = request.getAmount();
        Integer period = request.getPeriodInYears();
        BigDecimal interest = BigDecimal.valueOf(period).multiply(BigDecimal.valueOf(0.8));
        BigDecimal outcomeAmount =
                amount.multiply(interest.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP)).add(amount);

        CDAccountEntity account = new CDAccountEntity();
            account.setBalance(request.getAmount());
            account.setIban(Iban.random(CountryCode.BG).toString());
            account.setUser(user);
            account.setType(accountType);
            account.setInterest(interest);
            account.setPeriod(period);
            account.setExpirationDate(account.getCreatedOn().toLocalDate().plusYears(period));
            account.setOutcomeAmount(outcomeAmount);

        CDAccountEntity savedAccount = cdAccountRepository.save(account);

        return modelMapper.map(savedAccount, CDAccountResponse.class)
                .setFullName(account.getUser().getFullName());
    }

    /**
     * {@inheritDoc}
     * @param date the expiration date by which accounts are queried in the database.
     * @return a {@link List} of {@link CDAccountEntity}s.
     * @throws NoRecordsOfEntityInTheDatabase if no entries match the query criteria.
     */
    @Override
    public List<CDAccountEntity> getAllCDAccountsByDate(LocalDate date) {
        List<CDAccountEntity> accounts = cdAccountRepository.findAllByExpirationDate(date);
        if (accounts.isEmpty()) {
            throw new NoRecordsOfEntityInTheDatabase(CDAccountEntity.class.getSimpleName());
        } else {
            return accounts;
        }
    }

    /**
     * {@inheritDoc}
     * @param cdAccount the Certification of deposit account to be transformed.
     * @param balance the balance that will be set upon transformation of the account.
     * @return a {@link CheckingAccountEntity} made from the given Certification of deposit account.
     */
    @Override
    public CheckingAccountEntity transformCDAccountToCheckingAccount(CDAccountEntity cdAccount, BigDecimal balance) {
        CheckingAccountEntity checkingAccount = new CheckingAccountEntity();
        checkingAccount.setIban(cdAccount.getIban());
        checkingAccount.setUser(cdAccount.getUser());
        checkingAccount.setBalance(balance);
        checkingAccount.setType(accountTypeService.findAccountTypeByType("Checking"));
        checkingAccountService.saveAccount(checkingAccount);
        cdAccountRepository.delete(cdAccount);
        return checkingAccount;
    }

    /**
     * {@inheritDoc}
     * @param userId the id of the issuing user.
     * @param body a {@link TransactionCreationRequest} holding the parameters for the withdrawal.
     * @return a {@link TransactionResponse} with the information of the created transaction
     */
    @Override
    public TransactionResponse withdraw(Long userId, TransactionCreationRequest body) {
        CDAccountEntity cdAccount = cdAccountRepository.findByIban(body.getIban())
                .orElseThrow( (()-> new EntityNotFoundException("Certification of deposit account")));

        cdAccount.setBalance(cdAccount.getBalance().subtract(BigDecimal.valueOf(50)));

        transformCDAccountToCheckingAccount(cdAccount,cdAccount.getBalance());
        return transactionService.withdraw(userId, body);
    }

    @Override
    public List<CDAccountResponse> getAllCDAccountResponsesByUserId(Long userId, Long loggedInUserId) {
        if(isAdmin(loggedInUserId) && !userId.equals(loggedInUserId)) {
            throw new ResourceNotBelongingToUser("Checking account");
        }
        List<CDAccountEntity> allByUserId = this.cdAccountRepository.findAllByUser_Id(userId);

        if(allByUserId.isEmpty()){
            throw new NoRecordsOfEntityInTheDatabase("Certification of deposit account");
        }
        return allByUserId.stream()
                .map(cdAc -> modelMapper.map(cdAc,CDAccountResponse.class).setFullName(cdAc.getUser().getFullName()))
                .collect(Collectors.toList());
    }

    private boolean isAdmin(Long usId) {
        return this.userService.getUserEntityById(usId)
                .getRoles().stream().noneMatch(role -> role.getRole().equals("ADMIN"));
    }

}
