import java.util.ArrayList;
import java.util.Arrays;

class UserSolution {
	static int N;
	static int[] mGrowthTime;
	static int[][][] map;

	void init(int N, int mGrowthTime[])	{
		this.N = N;
		this.mGrowthTime = Arrays.copyOf(mGrowthTime, mGrowthTime.length);
		map = new int[3][N][N]; //0층에는 작물 카테고리 번호, 1층에는 시작시간, 2층에는 작물 키 
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				map[0][i][j] = -1;
			}
		}
	}

	int sow(int mTime, int mRow, int mCol, int mCategory) {
		if(map[0][mRow][mCol] != -1) return 0;
		
		map[0][mRow][mCol] = mCategory;
		map[1][mRow][mCol] = mTime;
		
		return 1;
	}

	int water(int mTime, int G, int mRow, int mCol, int mHeight, int mWidth) {
		int crops = 0;
		
		for(int i = 0; i < mHeight; i++) {
			int x = mRow + i; 
			for(int j = 0; j < mWidth; j++) {
				int y = mCol + j;
				if(map[0][x][y] == -1 || map[1][x][y] > mTime) continue;
				
				map[2][x][y] += G;
				crops++;
			}
		}
		
		return crops;
	}

	int harvest(int mTime, int L, int mRow, int mCol, int mHeight, int mWidth) {
		int crops = 0;
		ArrayList<int[]> harvestCrops = new ArrayList<>();
		
		for(int i = 0; i < mHeight; i++) {
			int x = mRow + i;
			for(int j = 0; j < mWidth; j++) {
				int y = mCol + j;
				int c = map[0][x][y];
				int h = 0;
				if(c == -1) continue;
				
				
				h = map[2][x][y] + ((mTime-map[1][x][y]) / mGrowthTime[c]);
				if(h < L || mTime < map[1][x][y]) return 0;
				harvestCrops.add(new int[]{x,y});
				
			}
		}
		
		for(int i = 0; i < harvestCrops.size(); i++) {
			int x = harvestCrops.get(i)[0];
			int y = harvestCrops.get(i)[1];
			
			map[0][x][y] = -1;
			map[1][x][y] = 0;
			map[2][x][y] = 0;
		}
		
		crops = harvestCrops.size();
		return crops;
	}
}
