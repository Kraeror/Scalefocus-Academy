package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.LoanTypeEntity;
import com.example.banksystem.models.responses.LoanTypeResponse;
import com.example.banksystem.repositories.LoanTypeRepository;
import com.example.banksystem.services.interfaces.LoanTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanTypeServiceImpl implements LoanTypeService {
    private final LoanTypeRepository loanTypeRepository;
    private final ModelMapper modelMapper;

    public LoanTypeServiceImpl(LoanTypeRepository loanTypeRepository, ModelMapper modelMapper) {
        this.loanTypeRepository = loanTypeRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     * @param id is the id of the entity.
     * @return
     */
    @Override
    public LoanTypeResponse getLoanTypeById(Long id) {
        return modelMapper.map(loanTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loan Type")),
                LoanTypeResponse.class);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public List<LoanTypeResponse> getAllLoanTypes() {
        List<LoanTypeEntity> loanTypeEntities = loanTypeRepository.findAll();
        if(loanTypeEntities.isEmpty()){
            throw new NoRecordsOfEntityInTheDatabase("Loan Type");
        }
        return loanTypeEntities.stream().map(type -> modelMapper.map(type,LoanTypeResponse.class))
                .collect(Collectors.toList());
    }


}
