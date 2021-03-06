--无返回值的存储过程
CREATE OR REPLACE PROCEDURE TESTA(PARA1 IN VARCHAR2,PARA2 IN VARCHAR2) AS
BEGIN 
  INSERT INTO HYQ.B_ID (I_ID,I_NAME) S (PARA1, PARA2);
END TESTA;

--有返回值的存储过程（非列表）
CREATE OR REPLACE PROCEDURE TESTB(PARA1 IN VARCHAR2,PARA2 OUT VARCHAR2) AS
BEGIN 
  SELECT INTO PARA2 FROM TESTTB WHERE I_ID= PARA1; 
END TESTB;

--返回列表
--由于oracle存储过程没有返回值，它的所有返回值都是通过out参数来替代的，列表同样也不例外.
--但由于是集合，所以不能用一般的参数，必须要用pagkage了.所以要分两部分，
CREATE OR REPLACE PACKAGE TESTPACKAGE  AS
TYPE Test_CURSOR IS REF CURSOR;
end TESTPACKAGE;

CREATE OR REPLACE PROCEDURE TESTC(p_CURSOR out TESTPACKAGE.Test_CURSOR) IS 
BEGIN
    OPEN p_CURSOR FOR SELECT * FROM HYQ.TESTTB;
END TESTC;

--