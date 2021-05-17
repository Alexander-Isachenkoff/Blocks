package project.generation;

import java.util.Random;

public class Generator
{
	private static Random rnd = new Random();

	public static int getInt(int min, int max)
	{
		return Math.abs(rnd.nextInt() % (max-min+1)) + min;
	}
}
