package ch.sr35.rummikub.asp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sr35.rummikub.common.IFigureBag;

public class GameStateMatcher {
	
	static Logger logger = LoggerFactory.getLogger(GameStateMatcher.class);
	
	public static List<IFigureBag> match(List<IFigureBag> tableOld, List<IFigureBag> tableNew)
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
			sumColumns.sort((a,b) -> a.val < b.val ? -1 : 1);
			
			while (sumColumns.size() > colsToRemove)
			{
				sumColumns.remove(sumColumns.size()-1);
			}
			sumColumns.sort((a,b) -> a.index > b.index ? -1 : 1); // sort indices downwards e.g. 10, 7, 4,3
			for (int q =0; q < colsToRemove;q++)
			{
				final int t = q;
				matchMatrix.forEach(e -> {
					e.remove(sumColumns.get(t).index);
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
			int zeroCnt=0;
			List<MaxVal> rowMatchSums = new ArrayList<MaxVal>();
			for (int c4=0;c4<matchMatrix.size();c4++)
			{
				matchMatrix.get(c4).stream().mapToDouble(e1 -> e1.doubleValue()).sum();
				rowMatchSums.add(new MaxVal(c4,(float)matchMatrix.get(c4).stream().mapToDouble(e1 -> e1.doubleValue()).sum() ));
			}
			rowMatchSums.sort((a,b) -> a.val < b.val ? -1 : 1);

			for (int c2=0;c2<colsToAdd;c2++)
			{
				matchMatrix.get(rowMatchSums.get(c2).index).set(tableOld.size()+zeroCnt, 1.0f);
				zeroCnt+=1;
			}

		}
		
		
		// find rows where no distinct maximums are found
		// add 1s at the columns where no distinct maximums have been found
		List<List<Integer>> distinctIndexes=new ArrayList<List<Integer>>();
		matchMatrix.forEach(e -> {
			float minVal=2.0f;
			float maxVal=-1.0f;

			for(int c=0;c<e.size();c++)
			{
				if (e.get(c) < minVal) { minVal=e.get(c); }
				if (e.get(c) > maxVal) { maxVal=e.get(c); }
			}
			List<Integer> maxAlongRow = new ArrayList<Integer>();
			for (int c=0;c<e.size();c++)
			{
				
				if(Math.abs(e.get(c)-maxVal)<0.001)
				{
					maxAlongRow.add(c);
				}
			}
			distinctIndexes.add(maxAlongRow);
		});
		
		boolean distinctionSuccessful=true;
		
		while (distinctionSuccessful)
		{
			distinctionSuccessful=false;
			List<Integer> distinctIndexesUnique = distinctIndexes.stream().filter(el -> el.size()==1).map(p -> p.get(0)).collect(Collectors.toList());
			for(int c=0;c<matchMatrix.size();c++)
			{
				if (distinctIndexes.get(c).size()>1)
				{
					List<Integer> indexCandidates = new ArrayList<Integer>();
					for (int q=0;q<distinctIndexes.get(c).size();q++)
					{
						final int cc = c;
						final int qq = q;
						if (!distinctIndexesUnique.stream().anyMatch(el -> distinctIndexes.get(cc).get(qq)==el))
						{
							indexCandidates.add(distinctIndexes.get(cc).get(qq));
						}
					}
					distinctIndexes.set(c, indexCandidates);
					if (indexCandidates.size()==1)
					{
						distinctionSuccessful=true;
						matchMatrix.get(c).set(indexCandidates.get(0), 1.0f);
						break;
					}
				}
			}
			if (!distinctionSuccessful && distinctIndexes.stream().anyMatch(el -> el.size()> 1))
			{
				distinctIndexes.stream().filter(e -> e.size() > 1).findFirst().ifPresent(c -> {
					while (c.size() > 1)
					{
						c.remove(c.size()-1);
					}
				});
			}
		}
		
		if (distinctIndexes.stream().anyMatch(e -> e.size() > 1))
		{
			logger.debug("failed to generate distinct indexes, return unsorted new state");
			return tableNew;
		}
		else
		{
			List<Integer> flattenedIndexes = distinctIndexes.stream().flatMap(e->e.stream()).collect(Collectors.toList());
			List<Integer> missingIndexes = new ArrayList<Integer>();
			List<Integer> multipleIndexes = new ArrayList<Integer>();
			for(int c=0;c<matchMatrix.size();c++)
			{
				final int cc = c;
				if(flattenedIndexes.stream().filter(e -> e==cc).count()==0)
				{
					missingIndexes.add(cc);
				}
				else if (flattenedIndexes.stream().filter(e -> e==cc).count()>1)
				{
					multipleIndexes.add(cc);
				}
			}
			while (multipleIndexes.size()>0)
			{
				for(int c=0;c<flattenedIndexes.size();c++)
				{
					if (multipleIndexes.contains(c))
					{
						flattenedIndexes.set(c, missingIndexes.get(0));
						missingIndexes.remove(0);
						final int cc = c;
						if (!(flattenedIndexes.stream().filter(e -> e==cc).count()>1))
						{
							multipleIndexes.remove(c);
						}
						break;
					}
				}
			}
			
			List<IFigureBag> orderedTableList = new ArrayList<IFigureBag>(Collections.nCopies(tableNew.size(), null));
			Iterator<Integer> mvIt = flattenedIndexes.iterator();
			int cnt =0;
			while (mvIt.hasNext())
			{
				Integer nxt = mvIt.next();
				orderedTableList.set(nxt, tableNew.get(cnt));
				cnt++;
			}
			return orderedTableList;	
		}
	}
		

	
	static class MaxVal {
		int index;
		float val;
		
		MaxVal(int i,float v)
		{
			this.index = i;
			this.val = v;
		}
	}

}
