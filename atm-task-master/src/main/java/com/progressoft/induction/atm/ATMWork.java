package com.progressoft.induction.atm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.*;
import java.util.Map;

import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;

public class ATMWork implements ATM, BankingSystem {

	@Override
	public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {
		
		if(amount.doubleValue()%5!=0) {
			 throw new InsufficientFundsException("Enter amount divisible by 5");
		}
		
		List<Banknote> notes=new ArrayList<>();

		double amounts=amount.doubleValue();
		Banknote fifty = Banknote.FIFTY_JOD;
		Banknote twenty = Banknote.TWENTY_JOD;
		Banknote ten = Banknote.TEN_JOD;
		Banknote five = Banknote.FIVE_JOD;
		double fiftyJod=fifty.getValue().doubleValue();
		double twentyJod=twenty.getValue().doubleValue();
		double tenJod=ten.getValue().doubleValue();
		double fiveJod=five.getValue().doubleValue();
//		System.out.println(fifty);
		
		
		
		int count=0;
		double total = fiftyJod*10+twentyJod*20+tenJod*100+fiveJod*100;
		System.out.println("Total "+total);
			if (total < amounts) {
				throw new InsufficientFundsException("Insufficient balance");
			} else {

				System.out.println("Method debit acount called");
				debitAccount(accountNumber, amount);
			

				
				int [] rs=new int[] {50,20,10,5};
				int [] noteCount=new int[4];
				for(int i=0;i<4;i++) {
				if(amounts>=rs[i]) {
					noteCount[i]=(int) (amounts/rs[i]);
					amounts=amounts-noteCount[i]*rs[i];
				}
				
			}
				//notes
				for(int i=0;i<4;i++) {
					if(noteCount[i]!=0) {
						System.out.println("Amounts "+rs[i]+" * "+noteCount[i]);
						if(rs[i]==50) {
							notes.add(fifty);
						}
						if(rs[i]==20) {
							notes.add(twenty);
						}
						if(rs[i]==10) {
							notes.add(ten);
						}
						if(rs[i]==5) {
							notes.add(five);
						}
					}
				}
					
				
				
			}
			System.out.println(notes);
			return notes;

		}

	

	@Override
	public BigDecimal getAccountBalance(String accountNumber) {
		BigDecimal balance = null;
		if (accountNumber == null) {

			throw new AccountNotFoundException("Account not found");
		}


		Map<String, Double> accountList = new HashMap<String, Double>();
		accountList.put("123456789", 1000.0);
		accountList.put("111111111", 1000.0);
		accountList.put("222222222", 1000.0);
		accountList.put("333333333", 1000.0);
		accountList.put("444444444", 1000.0);


		
		Iterator<Map.Entry<String, Double>> iterator=accountList.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, Double> val=iterator.next();
			System.out.println(val.getKey()+" "+val.getValue());
			if(val.getKey().equals(accountNumber)) {
			balance=BigDecimal.valueOf(val.getValue());
			}
			

		}
		
		System.out.println("balance " + balance);

		return balance;
	}

	@Override
	public void debitAccount(String accountNumber, BigDecimal amount) {

		// do all the things here only

		double remaining = 0;
		double withdrawAmt = amount.doubleValue();
		System.out.println("Amount to withdraw in double " + withdrawAmt);
		System.out.println("Method getBalance called in debit Account");
		BigDecimal total = getAccountBalance(accountNumber);
		double totalAmt = total.doubleValue();
		System.out.println("Total amount in bank " + totalAmt);
		if(withdrawAmt==totalAmt) {
			throw new InsufficientFundsException("you are withdraing all amount");
		}
		if (totalAmt < withdrawAmt) {
			throw new InsufficientFundsException("Balance not enough");
		}
		remaining = totalAmt - withdrawAmt;
		
//		System.out.println("Total remaining amount in bank " + remaining);
		withdrawAmt = remaining;
		System.out.println("Amount has been debited " + amount + " and your remaining balance is " + remaining);
	}

}
