package com.progressoft.induction.atm;

import java.math.BigDecimal;

public class Main {

	public static void main(String [] args) {
		ATMWork atm=new ATMWork();
		atm.withdraw("333333333", new BigDecimal("995"));

	}
}
 