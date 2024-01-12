package workplace;


public class gened
{
	
	
	final static int GENES_PER_CH = 5;
	final static int INTERVAL_MAX = 9;
	final static int INTERVAL_MIN = 0;
	final static int ITERATIONS = 10;
	final static double POP_KEEP = .8;
	final static int POP_SIZE = 10;
	final static double PROB_CROSSOVER = .9;
	final static double PROB_MUTATION = .15;
	final static double TOURNAMENT_SIZE = 3;
	final static int TARGET = 0; 
	
	
	public static int[][] generatePopulation()
	{
		int [][] individuals = new int[POP_SIZE][GENES_PER_CH];
		for(int i = 0; i < POP_SIZE; i ++)
		{
			for(int c = 0; c < GENES_PER_CH; c ++)
				individuals[i][c] = INTERVAL_MIN + (int)((Math.random() * (INTERVAL_MAX - INTERVAL_MIN) + 1));
		}
		return individuals; 
	}
	
	
	public static int[] evaluate(int [][] population)
	{
		int [] popScores = new int [POP_SIZE];
		for(int individual = 0; individual < POP_SIZE; individual ++)
		{
			int score = 0;
			for(int gene = 0; gene < GENES_PER_CH; gene ++)
			{
				score += population[individual][gene];
			}
			popScores[individual] = score;
		}
		return popScores;
	}
	
	
	public static int[] calcFitness(int [] popScores)
	{
		int [] popFitness = new int[POP_SIZE];
		for(int individual = 0; individual < POP_SIZE; individual ++)
		{
			int fitness = Math.abs(TARGET - popScores[individual]);
			popFitness[individual] = fitness;
		}
		return popFitness;
	}
	
	
	public static int[][] selectFittest(int [][] population, int [] popFitness)
	{
		int [][] fitterPopulation = new int[POP_SIZE][GENES_PER_CH];
		int count = 0;
		for(int i = 0; i < POP_SIZE * POP_KEEP; i ++)
		{
			int r = (int)(Math.random() * POP_SIZE);
			int best = popFitness[r];
			int competitorIndex = (int)(Math.random()  * (GENES_PER_CH));
			for(int member = 1; member < TOURNAMENT_SIZE; member ++)
			{
				if((int)popFitness[competitorIndex] < best);
				{
					competitorIndex = member;
					best = popFitness[competitorIndex];
				}
			}
			count += 1;
			fitterPopulation[count] = population[competitorIndex];
		}
		for(int c = 0; c < GENES_PER_CH * 1; c ++)
		{
			for(int a = 1; a < POP_SIZE; a ++)
			{
				for(int z = 0; z < GENES_PER_CH; z ++)
					fitterPopulation[a][z] = (int)((Math.random() * INTERVAL_MAX) + INTERVAL_MIN);
			}
		}
		return fitterPopulation;
	}
	
	public static int [][] crossover(int [][] population)
	{
		for (int individual = 0; individual < GENES_PER_CH; individual ++)
		{
			if (Math.random() <= PROB_CROSSOVER)
					{
						int [] parent1 = population[individual];
						int [] parent2 = population[individual]; 
						int r = (int)(Math.random() * GENES_PER_CH) + 1;
						int [] child1 = new int[GENES_PER_CH];  
						int [] child2 = new int[GENES_PER_CH];
						for(int chromosome = 0; chromosome < r; chromosome ++)
						{
							child1[chromosome] = parent1[chromosome];
							child2[chromosome] = parent2[chromosome];
						}
						for(int chromosome = r; chromosome < r - GENES_PER_CH; chromosome ++)
						{
							child1[chromosome] = parent1[chromosome];
							child2[chromosome] = parent2[chromosome];
						}
						population[individual] = child1;
						population[individual] = child2;
					}
		}
		return population;
	}
	
	
	public static int[][] mutation(int [][] population)
	{
		for(int individual = 0; individual < POP_SIZE; individual ++)
		{
			if(Math.random() <= PROB_MUTATION)
			{
				int [] ch = population[individual];
				int mutate = 1;
				for(int i = 0; i < 3; i ++)
				{
					int chose = (int)(Math.random() * 1);
					int position = (int)(Math.random() * GENES_PER_CH);
					int gene = ch[position];
					if(chose == 0)
					{
						if(gene - mutate >= INTERVAL_MIN)
							ch[position] = gene - mutate;
						else
							break;
					}
					else
					{
						if(gene + mutate <= INTERVAL_MAX)
							ch[position] = gene + mutate;
						else
							break;
					}
				}
			}
		}
		return population;
	}
	
	public static int[][] breed(int [][] population)
	{
		return mutation(crossover(population));
	}
			
			
	public static void main(String[] args)
	{
		int [][] population = generatePopulation();
		for(int generation = 0; generation <= ITERATIONS; generation ++)
		{
			int [] popScores = evaluate(population);
			int [] popFitness = calcFitness(popScores);
			int indexOfFittest = 0;
			if(generation % 1 == 0)
			{
				int leastFittest = popFitness[1], fittest = popFitness[1];
				for(int individual = 2; individual < POP_SIZE; individual++)
				{
					if(fittest < popFitness[individual])
					{
						fittest = popFitness[individual];
						indexOfFittest = individual;
					}
					leastFittest = Math.max(leastFittest, popFitness[individual]); 
				}
				System.out.printf("[G %d] score=(%d, %d): [", generation, fittest, leastFittest);
				for(int i = 0; i < GENES_PER_CH; i ++)
					System.out.print("" + population[indexOfFittest][i] + ",");
				System.out.print("]\n");
				if(fittest == 0)
					break;
			}
			population = breed(selectFittest(population, popFitness));
		}
	}
}
