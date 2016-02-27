package com.georgiev.payroll.transaction.impl;

import com.georgiev.payroll.domain.PaymentClassification;
import com.georgiev.payroll.domain.PaymentSchedule;
import com.georgiev.payroll.impl.HourlyClassification;
import com.georgiev.payroll.impl.WeeklySchedule;
import java.math.BigDecimal;

public class ChangeHourlyTransaction extends ChangeClassificationTransaction {

  private final BigDecimal hourlyRate;

  public ChangeHourlyTransaction(int employeeId, BigDecimal hourlyRate) {
    super(employeeId);
    this.hourlyRate = hourlyRate;
  }

  @Override
  protected PaymentClassification getClassification() {
    return new HourlyClassification(hourlyRate);
  }

  @Override
  protected PaymentSchedule getSchedule() {
    return new WeeklySchedule();
  }

}