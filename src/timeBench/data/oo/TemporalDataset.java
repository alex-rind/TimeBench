package timeBench.data.oo;

import java.util.Iterator;

/**
 * 
 * 
 * <p>
 * Added:         2011-09-28 / TL<br>
 * Modifications: 
 * </p>
 * 
 * @author Tim Lammarsch
 *
 */
public class TemporalDataset extends TemporalObject {
	public TemporalDataset(timeBench.data.relational.TemporalDataset data)
	{
		long totalInf = Long.MAX_VALUE;
		long totalSup = Long.MIN_VALUE;
        Iterator<timeBench.data.relational.TemporalObject> iTemporalObject = data.temporalObjects();
        while (iTemporalObject.hasNext()) {
        	timeBench.data.relational.TemporalObject relationalTemporalObject = iTemporalObject.next();
        	timeBench.data.oo.TemporalObject ooTemporalObject = new timeBench.data.oo.TemporalObject(relationalTemporalObject);
        	dataAspects.add(ooTemporalObject);
        	if (relationalTemporalObject.getTemporalElement().isAnchored()) {
        	    timeBench.data.relational.TemporalElement relationalTemporalElement = relationalTemporalObject.getTemporalElement();
                totalInf = Math.min(totalInf, relationalTemporalElement.getInf());
                totalSup = Math.max(totalInf, relationalTemporalElement.getSup());          
        	}
	    }
        temporalElement = new Interval(totalInf,totalSup);
	}
}