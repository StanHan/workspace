package demo.junit;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "../../../../applicationContext.xml", "../../../../applicationDatasource.xml" })
// 在普通spring的junit环境中配置事务
@TransactionConfiguration(transactionManager = "txMgr", defaultRollback = false)
@Transactional
public class JUnitDemo {

	@Test
	public void test1() {
		System.out.println("junit test1.");
	}

	@Test
	public void method() {
		org.junit.Assert.assertTrue(new ArrayList().isEmpty());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void outOfBounds() {
		new ArrayList<Object>().get(1);
	}

	@Test(timeout = 100)
	public void infinity() {
		while (true)
			;
	}

	@Test(timeout = 100)
	public void sleep100() throws InterruptedException {
		Thread.sleep(100);
	}

}
