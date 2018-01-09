function test(variable1) {
	// 如果variable1不是一个空对象，或者未定义，或者不等于空字符串
	if (variable1 !== null || variable1 !== undefined || variable1 !== '') {
		var variable2 = variable1;
	}
}