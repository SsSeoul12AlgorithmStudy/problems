import java.util.*;

class UserSolution {
	/*
	 * 어항 N개의 정보를 입력 받는 함수
	 * - 어항의 가로, 세로 동일
	 * - len은 높이
	 * - upShape는 결합판 값
	 * 
	 * 테케 처음에 호출
	 * */
	
	static class Container{
		int id;
		int height;
		int width;
		int[] lengths;
		int[] upShapes;
		
		public Container(int id, int height, int width, int[] lengths, int[] upShapes) {
			super();
			this.id = id;
			this.height = height;
			this.width = width;
			this.lengths = lengths;
			this.upShapes = upShapes;
		}
		
		
	}
	List<Container> containers;
	
	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {
		containers = new ArrayList<>();
		
		for(int i = 0; i < N; i++) {
			int[] lengths = Arrays.copyOf(mLengths[i], mWidth);
			int[] upShapes = Arrays.copyOf(mUpShapes[i], mWidth);
		
			containers.add(new Container(mIDs[i], mHeight, mWidth, lengths, upShapes));
		}
		
		containers.sort((a, b) -> Integer.compare(a.id, b.id));
		
//		System.out.printf("init : %s\n", containers);
	}

	/*
	 * 주어진 3개 구조물을 세울 수 있는 위치의 개수를 반환하는 함수. **우선순위 고려 안함** 없으면 0을 리턴
	 * - 구조물의 높이, 윗쪽 결합판, 아랫쪽 결합판 
	 * 
	 * 최대 1만회 호출
	 * */
	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
		int ret = 0;
		
		for(Container c : containers) {
			ret += findPlacableSpots(c, mLengths, mDownShapes);
		}
		
//		System.out.printf("check : %d\n", ret);
		
		return ret;
	}
	
	int findPlacableSpots(Container c, int[] lengths, int[] downShapes) {
		// 연속해서 3개가 한면이라도 겹치게 배치할 수 있으면 카운트
		int ret = 0;
		
		for(int i = 0; i < c.width - 2; i++) {
			if(placable(i, c, lengths, downShapes)) {
				
//				System.out.printf("check : found place i = %d\n clens = %s \n lens = %s \n cUps = %s \n downs= %s\n", 
//						i,
//						Arrays.toString(c.lengths), 
//						Arrays.toString(lengths), 
//						Arrays.toString(c.upShapes), 
//						Arrays.toString(downShapes));
				
				ret += 1;
			}
		}
		
		return ret;
	}
	
	boolean placable(int start, Container c, int[] lengths, int[] downShapes) {
		if(c.lengths[start] + lengths[0] > c.height) {
			return false;
		}
		if(c.upShapes[start] != downShapes[0]) {
			return false;
		}
		
		for(int i = 1; i <= 2; i++) {
			if(c.lengths[start + i] + lengths[i] > c.height) {
				return false;
			}
			if(c.upShapes[start + i] != downShapes[i]) {
				return false;
			}
			if(c.lengths[start + i] + lengths[i] <= c.lengths[start + i - 1] || c.lengths[start + i] >= c.lengths[start + i - 1] + lengths[i - 1]) {
				return false;
			}
		}
		
		return true;
	}

	/*
	 * 주어진 3개의 구조물을 최적의 위치에 설치한 후 위치를 반환하는 함수. 없으면 0을 리턴
	 * - 반환 값은 id * 1000 + 열위치 
	 * - 어항 id가 가장 작은 것, 가장 왼쪽 열에 있는 것
	 * 
	 * 최대 1000회 호출
	 * */
	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
		// TODO 위 함수와 중복 계산 제거?
		
		int ret = 0;
		
		for(Container c : containers) {
			int col = place(c, mLengths, mUpShapes, mDownShapes);
			if(col != -1) {
				ret = c.id * 1000 + col + 1; 
				break; 
			}
		}
		
//		System.out.printf("add : %d\n", ret);
		
		return ret;
	}
	
	int place(Container c, int lengths[], int upShapes[], int downShapes[]) {
		
		for(int i = 0; i < c.width - 2; i++) {
			if(placable(i, c, lengths, downShapes)) {
				
//				System.out.printf("place : \n clens = %s \n lens = %s \n cUps = %s \n downs= %s\n", 
//						Arrays.toString(c.lengths), 
//						Arrays.toString(lengths), 
//						Arrays.toString(c.upShapes), 
//						Arrays.toString(downShapes));
				
				for(int j = 0; j < 3; j++) {
					c.lengths[i + j] += lengths[j];
					c.upShapes[i + j] = upShapes[j];
				}
				
				return i;
			}
		}
		
		return -1;
	}

	/*
	 * 주어진 물을 이용해서 가장 높게 채울 수 있는 어항의 정보를 리턴. 없으면 id를 0으로 리턴
	 * - 어항 id, 최대 높이, 사용된 물의 양
	 * - 사용한 물이 제일 많은 것, 어항 id가 제일 작은 것 리턴
	 * 최대 100회 호출
	 * */
	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0;
		
		int maxHeight = 0;
		int maxUsed = 0;
		
		for(Container c : containers) {
			// 주어진 물을 사용해서 최대 높이를 계산
			// 물은 적어도 1이상 새용해야 한다
			int minLength = c.height;
			for(int i = 0; i < c.width; i++) {
				minLength = Math.min(minLength, c.lengths[i]);
			}
			
			// TODO 막대의 최소 길이보다 높게 채워야 물을 1이상 쓴다고 생각했는데 안된다. 왜?
			int lo = minLength;
			int hi = c.height + 1;
			
			while(lo + 1 < hi) {
				int mid = (lo + hi) / 2;
				
				if(usedWater(c.lengths, mid) <= mWater) {
					lo = mid;
				}
				else {
					hi = mid;
				}
			}
			
			int used = usedWater(c.lengths, lo);
			
//			System.out.printf("pour : %s\n lo = %d \n used=%d \n", c, lo, used);
			
			if(lo <= minLength) {
				continue;
			}
		
			if(lo > maxHeight || lo == maxHeight && used > maxUsed) {
				ret.ID = c.id;
				ret.height = lo;
				ret.used = used;
				
				maxHeight = lo;
				maxUsed = used;
			}
		}
		
//		System.out.printf("pour : %d %d %d\n", ret.ID, ret.height, ret.used);
		
		return ret;
	}
	
	int usedWater(int[] lengths, int fillHeight) {
		int ret = 0;
		
		for(int l : lengths) {
			ret += Math.max(fillHeight - l, 0);
		}
		
		return ret;
	}
}