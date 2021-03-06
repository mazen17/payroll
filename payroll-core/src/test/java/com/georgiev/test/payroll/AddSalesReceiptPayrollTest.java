package com.georgiev.test.payroll;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.georgiev.payroll.db.PayrollDatabase;
import com.georgiev.payroll.db.impl.InMemoryPayrollDatabase;
import com.georgiev.payroll.domain.Employee;
import com.georgiev.payroll.impl.Commissioned;
import com.georgiev.payroll.impl.SalesReceipt;
import com.georgiev.test.usecases.AddEmployee;
import com.georgiev.test.usecases.AddSalesReceipt;
import com.georgiev.test.utils.EmployeeData;
import com.georgiev.test.utils.EmployeeDataUtils;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class AddSalesReceiptPayrollTest {

  Map<String, Object> data;
  AddSalesReceipt addSR;
  AddEmployee addEmp;
  PayrollDatabase db;

  @Before
  public void setup() {
    db = new InMemoryPayrollDatabase();
    data = EmployeeData.getStandardDataForEmployee();
    addEmp = new AddEmployee();
    addSR = new AddSalesReceipt();
  }

  @Test
  public void shouldAddSalesReceipt() throws Exception {
    addEmp.addCommissionedEmployee(db, data);
    addSR.addSalesReceipt(db, data);
    Employee e = db.getEmployee(EmployeeDataUtils.getId(data));

    assertThat(e, is(notNullValue()));
    Commissioned cc = (Commissioned) e.getPayType();
    SalesReceipt sr = cc.getSalesReceipt(EmployeeDataUtils.getDate(data));
    assertThat(sr, is(notNullValue()));
    assertThat(sr.getAmount(), is(EmployeeDataUtils.getSoldAmount(data)));
  }

}
