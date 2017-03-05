package com.anjz.test;

import com.anjz.exception.BusinessException;

public class TestException {
	public TestException() {
	}

	boolean testEx() throws Exception {
		boolean ret = true;
		try {
			ret = testEx1();
		} catch (Exception e) {
			System.out.println("testEx, catch exception");
			ret = false;
			throw e;
		} finally {
			System.out.println("testEx, finally; return value=" + ret);
			return ret;
		}
	}

	boolean testEx1() throws Exception {
		boolean ret = true;
		try {
			ret = testEx2();
			if (!ret) {
				return false;
			}
			System.out.println("testEx1, at the end of try");
			return ret;
		} catch (Exception e) {
			System.out.println("testEx1, catch exception");
			ret = false;
			throw e;
		} finally {
			System.out.println("testEx1, finally; return value=" + ret);
			return ret;
		}
	}

	boolean testEx2() throws Exception {
		boolean ret = true;
		try {
			int b = 12;
			int c;
			for (int i = 2; i >= -2; i--) {
				c = b / i;
				System.out.println("i=" + i);
			}
			return true;
		} catch (Exception e) {
			System.out.println("testEx2, catch exception");
			ret = false;
			throw e;
		} finally {
			System.out.println("testEx2, finally; return value=" + ret);	
//			return ret;   //有返回值，程序正常结束
		}
		
	}

	public static void main(String[] args) {
		TestException testException1 = new TestException();
		try {
			testException1.testEx();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	static String procedure() {
//        try {
//            int a = 0;
//            int b = 42/a;
//        } catch(java.lang.ArithmeticException e) {
////            System.out.println("in procedure, catch ArithmeticException: " + e);
////            throw e;
//        	e.printStackTrace();
//        }
//        return "a";
//    }
//    public static void main(String args[]) {
//        try {
//            String str =procedure();
//            System.out.println(str);
//        } catch(java.lang. Exception e) {
//            System.out.println("in main, catch Exception: " + e);
//        }
//        System.out.println("666");
//    }
}