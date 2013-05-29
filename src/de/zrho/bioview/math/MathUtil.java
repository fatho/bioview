package de.zrho.bioview.math;

import java.util.Arrays;

public class MathUtil {

	public static int gcd(int a, int b) {
		while (b != 0) {
			int h = a % b;
			a = b;
			b = h;
		}
		
		return a;
	}
	
	public static int gcdMultiple(int[] nums) {
		if (nums.length == 0) return 0;
		
		Arrays.sort(nums);
		int result = nums[0];
		
		for (int i = 1; i < nums.length; ++i) {
			result = gcd(result, nums[i]);
		}
		
		return result;
	}
	
}
