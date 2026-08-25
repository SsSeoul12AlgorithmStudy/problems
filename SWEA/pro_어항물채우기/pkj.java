import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

class UserSolution {
	PriorityQueue<FishBowl> fishBowls;
	int width;
	int height;
	int N;
	
	class FishBowl {
		int id;
		int[] lengths;
		int[] tops;
		int maxH;
		
		FishBowl(int id, int[] lengths, int[] tops){
			this.id = id;
			this.lengths = Arrays.copyOf(lengths, lengths.length);
			this.tops = Arrays.copyOf(tops, tops.length);
			this.maxH = 0;
		}
	}

	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {
		fishBowls = new PriorityQueue<>(new Comparator<FishBowl>() {
			@Override
			public int compare(FishBowl f1, FishBowl f2) {
				return Integer.compare(f1.id, f2.id);
			}
		});
		
		this.N = N;
		this.width = mWidth;
		this.height= mHeight;
		
		for(int i = 0; i < N; i++) {
			int nID = mIDs[i];
			int[] nLen = new int[width]; //구조체 높이
			int[] nTop = new int[width]; //상판 번호
			for(int j = 0; j < width; j++) {
				nLen[j] = mLengths[i][j];
				nTop[j] = mUpShapes[i][j];
			}
			
			fishBowls.offer(new FishBowl(nID, nLen, nTop));
		}
		
		for(FishBowl fb : fishBowls) {
			int[] len = fb.lengths;
			for(int i = 0; i < len.length; i++) {
				if(len[i] > fb.maxH) fb.maxH = len[i];
			}
		}
	}

	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
		int result = 0;
		
		
		for(FishBowl fb : fishBowls) {
			int nID = fb.id;
			int[] nLen = fb.lengths;
			int[] nTop = fb.tops;
			
			for(int j = 0; j < width-2; j++) {
				if(nTop[j] == mDownShapes[0] && nTop[j+1] == mDownShapes[1] && nTop[j+2] == mDownShapes[2]) {
					if(nLen[j]+mLengths[0] > nLen[j+1] && nLen[j+2]+mLengths[2] > nLen[j+1] && nLen[j+1]+mLengths[1] > nLen[j] && nLen[j+1]+mLengths[1] > nLen[j+2])
						if(nLen[j]+mLengths[0] <= height && nLen[j+1]+mLengths[1] <= height && nLen[j+2]+mLengths[2] <= height)
							result++; 
				}	
				else continue;
			}
		}
		
		return result;
	}

	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
		int result = 0;
		PriorityQueue<FishBowl> tempQueue = new PriorityQueue<>(fishBowls);
		FishBowl temp = null;
		while(!tempQueue.isEmpty()) {
			FishBowl fb = tempQueue.poll();
			int nID = fb.id;
			int[] nLen = fb.lengths;
			int[] nTop = fb.tops;
			
			for(int j = 0; j < width-2; j++) {
				if(nTop[j] == mDownShapes[0] && nTop[j+1] == mDownShapes[1] && nTop[j+2] == mDownShapes[2]) {
					if(nLen[j]+mLengths[0] > nLen[j+1] && nLen[j+2]+mLengths[2] > nLen[j+1] && nLen[j+1]+mLengths[1] > nLen[j] && nLen[j+1]+mLengths[1] > nLen[j+2]) {
						if(nLen[j]+mLengths[0] <= height && nLen[j+1]+mLengths[1] <= height && nLen[j+2]+mLengths[2] <= height)
						{
							result = nID * 1000 + j + 1;
							nLen[j] += mLengths[0];
							nLen[j+1] += mLengths[1];
							nLen[j+2] += mLengths[2];
							nTop[j] = mUpShapes[0];
							nTop[j+1] = mUpShapes[1];
							nTop[j+2] = mUpShapes[2];
							if(nLen[j] > fb.maxH) fb.maxH = nLen[j];
							if(nLen[j+1] > fb.maxH) fb.maxH = nLen[j+1];
							if(nLen[j+2] > fb.maxH) fb.maxH = nLen[j+2];
							temp = new FishBowl(nID, nLen, nTop);
							temp.maxH = fb.maxH;
							break;
						}
					}
						
				}	
			}
			if(result != 0) break;
		}
		
		if(result != 0) {
			for(FishBowl fb : fishBowls) {
				if(fb.id == temp.id) {
					fb = temp;
					break;
				}
			}
		}
		return result;
	}

	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0; 
		
		for(FishBowl fb : fishBowls) {
			int id = fb.id;
			int target = fb.maxH; //물을 채운 높이
			int[] len = Arrays.copyOf(fb.lengths, fb.lengths.length);
			int usedWater = 0;
			
			while(target > 1) {
				usedWater = target * width;
				for(int i = 0; i < width; i++) {
					if(len[i] > target) {
						len[i] = target;
					}
					usedWater -= len[i];
				}
				if(usedWater != 0 && usedWater <= mWater) break;
				else target--;
			}
			
			target += ((mWater-usedWater) / width);
			usedWater += (((mWater-usedWater)/width)*width);
			if(target > height) {
				usedWater -= (target-height)*width;
				target = height;
			}
			
			if(target > ret.height) {
				ret.ID = id;
				ret.used = usedWater;
				ret.height = target;
			}
			else if(target == ret.height) {
				if(ret.used < usedWater) {
					ret.ID = id;
					ret.used = usedWater;
					ret.height = target;
				}
				else if(ret.used == usedWater) {
					if(ret.ID > id) {
						ret.ID = id;
						ret.used = usedWater;
						ret.height = target;
					}
				}
			}
		}
		
		return ret;
	}
}
