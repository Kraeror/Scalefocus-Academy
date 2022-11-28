package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.models.entities.LoanEntity;
import com.example.banksystem.models.entities.LoanTypeEntity;
import com.example.banksystem.models.requsts.CalculateLoanRequest;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.responses.CalculateLoanResponse;
import com.example.banksystem.repositories.LoanTypeRepository;
import com.example.banksystem.services.interfaces.LoanCalculatorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class LoanCalculatorServiceImpl implements LoanCalculatorService {
	private final LoanTypeRepository loanTypeRepository;
	private final ModelMapper mapper;
	private final int scale = 36;
	private final BigDecimal salaryCondition = BigDecimal.valueOf(0.4);


	public LoanCalculatorServiceImpl(LoanTypeRepository loanTypeRepository, ModelMapper mapper) {
		this.loanTypeRepository = loanTypeRepository;
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CalculateLoanResponse calculateLoan(CalculateLoanRequest request) {
		LoanTypeEntity loanType = loanTypeRepository.findByName(request.getLoanType())
						.orElseThrow(() -> new EntityNotFoundException(request.getLoanType()));
		BigDecimal loanAmount = request.getLoanAmount();
		int periodInMonths = request.getPeriod();
		BigDecimal loanInterestRate = loanType.getInterestRate()
						.divide(BigDecimal.valueOf(100), scale, RoundingMode.HALF_UP);
		LoanEntity loanEntity = getLoanInformation(loanAmount, periodInMonths, loanInterestRate, loanType);

		return mapper.map(loanEntity, CalculateLoanResponse.class);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param applyRequest {@link LoanApplyRequest} the required data for the loan
	 * @param userId       the id of the currently logged in user.
	 * @return
	 */
	@Override
	public LoanEntity createLoanAppliement(LoanApplyRequest applyRequest, Long userId) {
		LoanTypeEntity loanType = loanTypeRepository.findByName(applyRequest.getLoanType())
						.orElseThrow(() -> new EntityNotFoundException(applyRequest.getLoanType()));
		BigDecimal loanAmount = applyRequest.getLoanAmount();
		int periodInMonths = applyRequest.getPeriod();
		BigDecimal loanInterestRate = loanType.getInterestRate()
						.divide(new BigDecimal(100), scale, RoundingMode.HALF_UP);

		return getLoanInformation(loanAmount, periodInMonths, loanInterestRate, loanType);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param loanAmount       the amount of the loan the user is applying for
	 * @param periodInMonths   the term of the loan
	 * @param loanInterestRate the loan interest rate determined by the bank depending on the type of loan
	 * @param loanType         the type of the loan
	 * @return
	 */
	public LoanEntity getLoanInformation(BigDecimal loanAmount, int periodInMonths,
	                                     BigDecimal loanInterestRate, LoanTypeEntity loanType) {
		BigDecimal loanMonthlyFee = loanType.getMonthlyFee();
		BigDecimal loanConsiderationFee = loanType.getConsiderationFee();

		BigDecimal monthlyPayment = this.calculateMonthlyPayment(loanAmount, loanInterestRate, periodInMonths);
		BigDecimal totalLoanAmountSum = calculateTotalAmountSum(loanAmount, monthlyPayment, periodInMonths,
						loanMonthlyFee, loanConsiderationFee);
		LoanEntity loanEntity = new LoanEntity()
						.setLoanType(loanType)
						.setBeginningLoanAmount(loanAmount)
						.setRemainingLoanAmount(loanAmount)
						.setPeriodInMonths(periodInMonths)
						.setMonthlyPayment(monthlyPayment)
						.setTotalAmountSum(totalLoanAmountSum)
						.setApproved(true);

		LocalDate dueDate = loanEntity.getCreatedOn().toLocalDate().plusMonths(loanEntity.getPeriodInMonths());
		loanEntity.setDueDate(dueDate);

		return loanEntity;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal calculateTotalAmountSum(BigDecimal loanAmount, BigDecimal monthlyPayment, int periodInMonths,
	                                          BigDecimal loanMonthlyFee, BigDecimal considerationFee) {
		BigDecimal totalMonthlyPayment = calculateTotalMonthlyPayment(monthlyPayment, periodInMonths);
		BigDecimal totalMonthlyFeeCosts = loanMonthlyFee.multiply(BigDecimal.valueOf(periodInMonths));

		return totalMonthlyPayment.add(totalMonthlyFeeCosts).add(considerationFee);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal calculateTotalInterestPayment(BigDecimal loanAmount,
													BigDecimal monthlyPayment,
													int periodInMonths) {
		BigDecimal totalMonthlyPayment = calculateTotalMonthlyPayment(monthlyPayment, periodInMonths);

		return totalMonthlyPayment.subtract(loanAmount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal calculateTotalMonthlyPayment(BigDecimal monthlyPayment, int periodInMonths) {
		return monthlyPayment.multiply(BigDecimal.valueOf(periodInMonths));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal loanInterestRate, int periodInMonths) {
		int monthsInOneYear = 12;
		BigDecimal monthlyInterestRate =
							loanInterestRate.divide(BigDecimal.valueOf(monthsInOneYear), scale, RoundingMode.HALF_UP);

		return amortizedLoanFormula(loanAmount, monthlyInterestRate, periodInMonths);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal amortizedLoanFormula(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int periodInMonths) {
		BigDecimal mathPow = (monthlyInterestRate.add(BigDecimal.ONE)).pow(periodInMonths);

		return loanAmount.multiply(monthlyInterestRate)
						.multiply(mathPow)
						.divide(mathPow.subtract(BigDecimal.ONE), scale, RoundingMode.HALF_UP);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkSalary(LoanApplyRequest request) {
		BigDecimal salary = request.getSalary();
		LoanTypeEntity loanType = loanTypeRepository.findByName(request.getLoanType())
						.orElseThrow(() -> new EntityNotFoundException(request.getLoanType()));

		BigDecimal loanAmount = request.getLoanAmount();
		BigDecimal loanInterestRate = loanType.getInterestRate()
						.divide(new BigDecimal(100), scale, RoundingMode.HALF_UP);
		int periodInMonths = request.getPeriod();

		BigDecimal monthlyPayment = this.calculateMonthlyPayment(loanAmount, loanInterestRate, periodInMonths);
		BigDecimal calculateSalary = salary.multiply(salaryCondition);

		if (monthlyPayment.compareTo(calculateSalary) > 0) {
			throw new IllegalArgumentException("Your salary does not meet the loan requirements!");
		}

		return monthlyPayment.compareTo(calculateSalary) <= 0;
	}
}
