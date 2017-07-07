package demo.stan;

import java.util.Date;

public class DaiKuan {

	public static void main(String[] args) {
		Date date1 = new Date();
		int a =0;
		for (int i = 0; i <= 300_0000; i++) {
			a++;
		}
		System.out.println(a);
		Date date2 = new Date();
		System.out.println(date2.getTime() - date1.getTime());
//		processInterestRate_CCB(10_0000.0, 12, 0.0032);
//		processInterestRate_CCB(10_0000.0, 24, 0.0032);
//		processInterestRate_CCB(10_0000.0, 36, 0.0032);
		// System.out.println("===================================");
//		processInterestRate_BOS(10_0000.0, 24, 0.077);
//		processInterestRate_BOS(10_0000.0, 36, 0.115);
	}

	/**
	 * 上海银行装修贷，提前收所有的手续费，按月平摊还本金。
	 * <p>
	 * 手续费=贷款总额*手续费率
	 * 
	 * @param amount
	 *            贷款总额
	 * @param months
	 *            贷款期数
	 * @param lixi
	 *            月手续费率
	 */
	public static void processMonthly_SHYH(double amount, int months, double lixi) {
		System.out.println("------上海银行贷款方案分析------");
		System.out.println("一共贷款：" + amount + "元");
		System.out.println("分" + months + "个月来还款");
		System.out.println("手续费率为：" + lixi);
		double avg = amount / months;
		System.out.println("平均每月还款：本金 " + avg + "元");
		double shouXuFei = lixi * amount;
		System.out.println("手续费为：" + shouXuFei + "，和第一期还款一起还");

		double yue_yuan = 0.0;
		for (int i = 1; i <= months + 1; i++) {
			double tmp = amount - (i - 1) * avg;

			System.out.println("第" + i + "个月持有银行贷款为：" + tmp);
			yue_yuan += tmp;
		}
		System.out.println(months + "月内持有贷款量：" + yue_yuan + " 月*元");

		System.out.println("我的还款：");
		double huanKuan_yue_yuan = shouXuFei * (months - 1);
		System.out.println(months + "月内上交手续费：" + huanKuan_yue_yuan + " 月*元");
		System.out.println(months + "月的资金占用率为：" + huanKuan_yue_yuan / yue_yuan);
		System.out.println("实际" + months / 12 + "年利息年利率为：" + shouXuFei * 12 / yue_yuan);
	}

	/**
	 * 建设银行装修贷，按月收手续费。
	 * <p>
	 * 手续费=贷款总额*月手续费率
	 * 
	 * @param amount
	 *            贷款总额
	 * @param months
	 *            贷款期数
	 * @param rate
	 *            月手续费率
	 */
	public static void processInterestRate_CCB(double amount, int months, double rate) {
		System.out.println("------建设银行贷款方案分析------");
		System.out.println("一共贷款：" + amount + "元");
		System.out.println("分" + months + "期来还款（一期为一个月）");
		System.out.println("每期手续费率为：" + rate);
		double needToPayInterestMonthly = rate * amount;// 每月偿还利息
		double interest = needToPayInterestMonthly * months;
		System.out.println("每期手续费为：" + needToPayInterestMonthly + "元," + months + "期共：" + interest + "元");
		double needToPayBalanceMonthly = amount / months;// 每月偿还本金
		double paymentMonthly = needToPayInterestMonthly + needToPayBalanceMonthly;// 每月偿还金额
		System.out.println("每月还款：本金 " + needToPayBalanceMonthly + " 元+每期手续费 " + needToPayInterestMonthly + "元,共计 "
				+ paymentMonthly+"元");
		double moneyXmonth = 0.0;
		int actualMonths = 0;
		for (int i = 1; i <= months+1; i++) {
			double tmp = amount - (i - 1) * paymentMonthly;
			if (tmp < 0) {
				actualMonths = i-1;
				break;
			}
			System.out.println("第" + i + "个月持有银行贷款为：" + tmp);
			moneyXmonth += tmp;
		}
		double moneyXyear = moneyXmonth / 12;
		System.out.println(actualMonths + "月内持有贷款量：" + moneyXmonth + " 元*月=" + moneyXyear + "元*年");
		System.out.println("实际" + months / 12 + "年贷款的年利率为：" + interest / moneyXyear);
	}

	/**
	 * 上海银行装修贷，提前收所有的手续费，按月平摊还本金。
	 * <p>
	 * 手续费=贷款总额*手续费率
	 * 
	 * @param amount
	 *            贷款总额
	 * @param months
	 *            贷款期数
	 * @param rate
	 *            月手续费率
	 */
	public static void processInterestRate_BOS(double amount, int months, double rate) {
		System.out.println("------上海银行贷款方案分析------");
		System.out.println("一共贷款：" + amount + "元");
		System.out.println("分" + months + "个月来还款");
		System.out.println("手续费率为：" + rate);
		double amount_paymentMonthly = amount / months;// 月还款额
		System.out.println("平均每月还款：本金 " + amount_paymentMonthly + "元");
		double interest = rate * amount;// 利息
		System.out.println("手续费为：" + interest + "，和第一期还款一起还");
		int actualMonths = 0;
		double moneyXmonth = 0.0;
		for (int i = 1; i <= months; i++) {
			double tmp = 0.0;
			if (i == 1) {
				tmp = amount;
			} else {
				tmp = amount - interest - (i - 1) * amount_paymentMonthly;
			}
			if (tmp < 0) {
				actualMonths = i-1;
				break;
			}
			System.out.println("第" + i + "个月持有银行贷款为：" + tmp);
			moneyXmonth += tmp;
		}
		double moneyXyear = moneyXmonth / 12;
		System.out.println(actualMonths + "月内持有贷款量：" + moneyXmonth + " 元*月=" + moneyXyear + "元*年");
		System.out.println("实际" + months / 12 + "年贷款的年利率为：" + interest / moneyXyear);
	}
}
