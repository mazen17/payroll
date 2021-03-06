package com.georgiev.test.payroll;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.georgiev.payroll.db.PayrollDatabase;
import com.georgiev.payroll.db.impl.InMemoryPayrollDatabase;
import com.georgiev.payroll.domain.Employee;
import com.georgiev.payroll.domain.PayDisposition;
import com.georgiev.payroll.domain.PaySchedule;
import com.georgiev.payroll.domain.PayType;
import com.georgiev.payroll.domain.UnionMembership;
import com.georgiev.payroll.impl.BiweeklySchedule;
import com.georgiev.payroll.impl.Commissioned;
import com.georgiev.payroll.impl.HoldMethod;
import com.georgiev.payroll.impl.Hourly;
import com.georgiev.payroll.impl.MonthlySchedule;
import com.georgiev.payroll.impl.NoMember;
import com.georgiev.payroll.impl.Salaried;
import com.georgiev.payroll.impl.WeeklySchedule;
import com.georgiev.test.usecases.AddEmployee;
import com.georgiev.test.utils.EmployeeData;
import com.georgiev.test.utils.EmployeeDataUtils;
import com.georgiev.util.Constants;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class AddEmployeePayrollTest {

  AddEmployee addEmp;
  Map<String, Object> data;
  PayrollDatabase db;

  @Before
  public void setup() {
    db = new InMemoryPayrollDatabase();
    data = EmployeeData.getStandardDataForEmployee();
    addEmp = new AddEmployee();
  }

  @Test
  public void shouldAddSalariedEmployee() throws Exception {
    addEmp.addSalariedEmployee(db, data);
    Employee e = db.getEmployee(EmployeeDataUtils.getId(data));
    assertThat(e.getName(), is("Bob"));

    PayType pc = e.getPayType();
    assertThat(pc, instanceOf(Salaried.class));

    Salaried sc = (Salaried) pc;
    assertThat(sc.getSalary(), is((BigDecimal) data.get(Constants.SALARY.name())));

    PaySchedule ps = e.getPaySchedule();
    assertThat(ps, instanceOf(MonthlySchedule.class));

    PayDisposition pm = e.getPayDisposition();
    assertThat(pm, instanceOf(HoldMethod.class));

    UnionMembership af = e.getUnionMembership();
    assertThat(af, instanceOf(NoMember.class));
  }

  @Test
  public void shouldAddCommissionedEmployee() throws Exception {
    addEmp.addCommissionedEmployee(db, data);

    Employee e = db.getEmployee(EmployeeDataUtils.getId(data));
    assertThat(e.getName(), is("Bob"));

    PayType pc = e.getPayType();
    assertThat(pc, instanceOf(Commissioned.class));

    Commissioned cc = (Commissioned) pc;
    assertThat(cc.getBasePay(), is(((BigDecimal) data.get(Constants.BASE_PAY.name())).setScale(2)));
    assertThat(cc.getCommissionRate(), is((BigDecimal) data.get(Constants.COMMISSION_RATE.name())));

    PaySchedule ps = e.getPaySchedule();
    assertThat(ps, instanceOf(BiweeklySchedule.class));

    PayDisposition pm = e.getPayDisposition();
    assertThat(pm, instanceOf(HoldMethod.class));

    UnionMembership af = e.getUnionMembership();
    assertThat(af, instanceOf(NoMember.class));
  }

  @Test
  public void shouldAddHourlyEmployee() throws Exception {
    addEmp.addHourlyEmployee(db, data);
    Employee e = db.getEmployee(EmployeeDataUtils.getId(data));

    assertThat(e.getName(), is("Bob"));

    PayType pc = e.getPayType();
    assertThat(pc, instanceOf(Hourly.class));

    Hourly hc = (Hourly) pc;
    assertThat(hc.getHourlyRate(), is((BigDecimal) data.get(Constants.HOURLY_RATE.name())));

    PaySchedule ps = e.getPaySchedule();
    assertThat(ps, instanceOf(WeeklySchedule.class));

    PayDisposition pm = e.getPayDisposition();
    assertThat(pm, instanceOf(HoldMethod.class));

    UnionMembership af = e.getUnionMembership();
    assertThat(af, instanceOf(NoMember.class));
  }

}
