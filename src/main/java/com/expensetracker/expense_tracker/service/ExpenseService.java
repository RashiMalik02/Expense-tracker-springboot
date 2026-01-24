package com.expensetracker.expense_tracker.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expensetracker.expense_tracker.dto.ExpenseRequest;
import com.expensetracker.expense_tracker.dto.ExpenseResponse;
import com.expensetracker.expense_tracker.model.Expense;
import com.expensetracker.expense_tracker.model.User;
import com.expensetracker.expense_tracker.repository.ExpenseRepository;
import com.expensetracker.expense_tracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepo;
    private final UserRepository userRepo;
    
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        
        User user = userRepo.findByEmail(email).orElseThrow();
        return user;
    }

    @Transactional
    public ExpenseResponse addExpense(ExpenseRequest req){
        User user = getAuthenticatedUser();

        Expense expense = Expense.builder()
                    .amount(req.getAmount())
                    .category(req.getCategory())
                    .description(req.getDescription())
                    .date(req.getDate())
                    .user(user)
                    .build();
        
        Expense savedExpense = expenseRepo.save(expense);

        return ExpenseResponse.builder()
                .id(savedExpense.getId())
                .amount(savedExpense.getAmount())
                .category(savedExpense.getCategory())
                .description(savedExpense.getDescription())
                .date(savedExpense.getDate())
                .build();
    }

    public List<ExpenseResponse> getExpenses(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(
            () -> new RuntimeException("Authenticated User Not Found")
        );

        return expenseRepo.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public ExpenseResponse updateExpense(Long expenseId, ExpenseRequest req){

        User user = getAuthenticatedUser();

        Expense expense = expenseRepo.findById(expenseId).orElseThrow(
            () -> new RuntimeException("Expense Not Found")
        );

        if(!expense.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not allowed to delete this expense");
        }

        expense.setAmount(req.getAmount());
        expense.setCategory(req.getCategory());
        expense.setDescription(req.getDescription());
        expense.setDate(req.getDate());

        return mapToResponse(expense);
    }

    @Transactional
    public void delete(Long expenseId){
        User user = getAuthenticatedUser();

        Expense expense = expenseRepo.findById(expenseId).orElseThrow(
            () -> new RuntimeException("Expense Not Found")
        );

        if(!expense.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not allowed to delete this expense");
        }

        expenseRepo.delete(expense);
    }


    private ExpenseResponse mapToResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .description(expense.getDescription())
                .date(expense.getDate())
                .build();
    }
}
