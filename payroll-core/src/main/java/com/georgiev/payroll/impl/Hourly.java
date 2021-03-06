package com.georgiev.payroll.impl;

import static java.math.BigDecimal.ROUND_HALF_UP;

import com.georgiev.payroll.domain.AbstractPayType;
import com.georgiev.payroll.domain.Paycheck;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Hourly extends AbstractPayType {

  private final BigDecimal hourlyRate;
  private final Map<Date, TimeCard> timeCards = new HashMap<Date, TimeCard>();

  public Map<Date, TimeCard> getTimeCards() {
    return timeCards;
  }

  public Hourly(BigDecimal hourlyRate) {
    this.hourlyRate = hourlyRate;
  }

  public BigDecimal getHourlyRate() {
    return hourlyRate;
  }

  public TimeCard getTimeCard(Date date) {
    return timeCards.get(date);
  }

  public void addTimeCard(TimeCard timeCard) {
    timeCards.put(timeCard.getDate(), timeCard);
  }

  @Override
  public BigDecimal calculatePay(Paycheck pc) {
    BigDecimal totalPay = BigDecimal.ZERO;
    for (TimeCard tc : timeCards.values()) {
      if (isInPayPeriod(tc.getDate(), pc)) {
        totalPay = totalPay.add(calculatePayForTimeCard(tc));
      }
    }
    return totalPay.setScale(2);
  }

  private BigDecimal calculatePayForTimeCard(TimeCard tc) {
    BigDecimal hours = tc.getHours();
    BigDecimal overtime = hours.subtract(BigDecimal.valueOf(8)).max(BigDecimal.ZERO);
    BigDecimal straightTime = hours.subtract(overtime);
    return straightTime.multiply(hourlyRate)
                       .add(overtime.multiply(hourlyRate).multiply(new BigDecimal("1.5")))
                       .setScale(2, ROUND_HALF_UP);
  }

}
