package com.czl.random;

import java.util.Random;

/**
 * @author Cao Zhili
 * @date 2015年4月1日
 */
public class RandomTest {

	public static void main(String[] args) {
		Random rd = new Random();
		
		for (int i = 0; i < 20; i++) {
			System.out.println(rd.nextInt(10));
		}
	}
}
