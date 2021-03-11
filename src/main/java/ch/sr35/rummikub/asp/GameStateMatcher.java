package ch.sr35.rummikub.asp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.sr35.rummikub.common.IFigureBag;

public class GameStateMatcher {
	
	public List<IFigureBag> match(List<IFigureBag> tableOld, List<IFigureBag> tableNew)
	{
		if (tableOld==null)
		{
			return tableNew;
		}
		else if (tableOld.size()==0)
		{
			return tableNew;
		}
		
		// construct the mapping matrix 1st dimension: the entry in tableNew
		// 2nd dimension: the entry in tableOld
		List<List<Float>> matchMatrix = new ArrayList<List<Float>>();
		tableNew.forEach(el -> {
			matchMatrix.add(new ArrayList<Float>());
			tableOld.forEach(el2 -> {
				matchMatrix.get(matchMatrix.size()-1).add(el.match(el2));
			});
		});
		
		// either shrink or expand the matrix depending if the number of new compositions is smaller or bigger
		if (tableNew.size()<tableOld.size())
		{
			// remove columns with the lowest match
			int colsToRemove = tableOld.size()-tableNew.size();
			List<MaxVal> sumColumns = new ArrayList<MaxVal>();
			for (int ocnt=0;ocnt<tableOld.size();ocnt++)
			{
				float sumColumn=0.0f;
				for (int ncnt=0;ncnt<tableNew.size();ncnt++)
				{
					sumColumn += matchMatrix.get(ncnt).get(ocnt);
				}
				sumColumns.add(new MaxVal(ocnt,sumColumn));
			}
			sumColumns.sort((a,b) -> a.val > b.val ? -1 : 1);
			
			while (sumColumns.size() > colsToRemove)
			{
				sumColumns.remove(sumColumns.size()-1);
			}
			sumColumns.sort((a,b) -> a.index > b.index ? -1 : 1); // sort indices downwards e.g. 10, 7, 4,3
			for (int q =0; q < colsToRemove;q++)
			{
				final int t = q;
				matchMatrix.forEach(e -> {
					e.remove(t);
				});
			}
		}
		else if (tableOld.size() < tableNew.size())
		{
			int colsToAdd = tableNew.size() - tableOld.size();
			List<Float> zerosToAdd = new ArrayList<Float>();
			for (int q=0;q<colsToAdd;q++)
			{
				zerosToAdd.add(0.0f);
			}
			matchMatrix.forEach(e -> e.addAll(zerosToAdd));
		}

		// get the maximum match value including index for each new element
		Stream<MaxVal> maximums = matchMatrix.stream().map(el -> {
			int idx = IntStream.range(0,el.size()).reduce((a,b) -> el.get(a) < el.get(b) ? b: a).getAsInt(); 
			return new MaxVal(idx,el.get(idx));
		});

		
		// relaxate the match Matrix: if two or more indices are the same replace all but the best with the second best option
		boolean conflictless=false;
		
		while(!conflictless)
		{
			final List<MaxVal> maxVals = maximums.collect(Collectors.toList());
			
			conflictless = maximums.filter(p -> Collections.frequency(maxVals, p.index) > 1).count() ==0;
			
			Optional<MaxVal> firstDuplicateIndex = maxVals.stream().filter(p -> Collections.frequency(maxVals, p.index) > 1).findFirst();
			
			if (firstDuplicateIndex.isPresent())
			{
				int[] sameIndexIdx =  IntStream.range(0, maxVals.size()).filter(i -> maxVals.get(i).index== firstDuplicateIndex.get().index)
						.toArray();
				
				List<MaxVal> sameIdxMaximums = new ArrayList<MaxVal>();
				for(int c=0;c<sameIndexIdx.length;c++)
				{
					sameIdxMaximums.add(maxVals.get(sameIndexIdx[c]));
					
					MaxVal mvCurrent = maxVals.get(sameIndexIdx[c]);
					matchMatrix.get(sameIndexIdx[c]).stream().dropWhile(p -> Math.abs(p.floatValue()-mvCurrent.val) < 0.001);
				}
				double bestVal = sameIdxMaximums.stream().mapToDouble(e -> (double)e.val).max().getAsDouble();
				if (bestVal > 0.0)
				{
					for(int c=0;c<sameIndexIdx.length;c++)
					{
						if (sameIdxMaximums.get(c).val < bestVal)
						{
							matchMatrix.get(sameIndexIdx[c]).set(sameIdxMaximums.get(c).index,(float)0.0);
						}
					}
				}
				else
				{
					// place all at the end
					for(int c=0;c<sameIndexIdx.length;c++)
					{
						for (int q=0;q<c;q++)
						{
							matchMatrix.get(sameIndexIdx[c]).add((float) 0.0);
						}
						matchMatrix.get(sameIndexIdx[c]).add((float) 1.0);
					}
				}
				maximums = matchMatrix.stream().map(el -> {
					int idx = IntStream.range(0,el.size()).reduce((a,b) -> el.get(a) < el.get(b) ? b: a).getAsInt(); 
					return new MaxVal(idx,el.get(idx));
				});					
			}
		}
		
		// rearrange the new FigureBag List
		//int maxIdx = maximums.mapToInt(e -> e.index).max().getAsInt();
		List<IFigureBag> orderedTableList = new ArrayList<IFigureBag>();
		Iterator<MaxVal> mvIt = maximums.iterator();
		int cnt =0;
		while (mvIt.hasNext())
		{
			MaxVal nxt = mvIt.next();
			orderedTableList.set(nxt.index, tableNew.get(cnt));
			cnt++;
		}
		return orderedTableList;
		
	}
		

	
	class MaxVal {
		int index;
		float val;
		
		MaxVal(int i,float v)
		{
			this.index = i;
			this.val = v;
		}
	}

}
