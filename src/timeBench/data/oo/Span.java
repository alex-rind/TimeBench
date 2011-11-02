package timeBench.data.oo;

import timeBench.calendar.Granularity;
import timeBench.data.TemporalDataException;

/**
 *  This class represents a span.
 * 
 * <p>
 * Added:          2011-07-19 / TL<br>
 * Modifications: 
 * </p>
 * 
 * @author Tim Lammarsch
 *
 */
public class Span extends UnanchoredTemporalElement {
	private int granulesWhenDynamic = 0;
	
	protected Span(timeBench.data.relational.TemporalElement relationalTemporalElement)  throws TemporalDataException {
		super(relationalTemporalElement);
		if (relationalTemporalElement.getKind() != timeBench.data.relational.TemporalDataset.PRIMITIVE_SPAN)
			throw new TemporalDataException("Cannot generate an Span object from a temporal element that is not a span.");
	}
	
	public Instant before(Instant anchor,Granularity granularity) throws TemporalDataException {
		if (granularity == null)
			granularity = getGranularity();
		
		long timeStamp = granularity.before(anchor.getInf(),getDuration());
		return new Instant(timeStamp,timeStamp,granularity);
	}
	
	
	public Instant after(Instant anchor,Granularity granularity) throws TemporalDataException {
		if (granularity == null)
			granularity = getGranularity();
		
		long timeStamp = granularity.after(anchor.getSup(),getDuration());
		return new Instant(timeStamp,timeStamp,granularity);
	}
	
	
	public long getDuration() {
		return relationalTemporalElement == null ? granulesWhenDynamic : ((timeBench.data.relational.AnchoredTemporalElement) relationalTemporalElement).getInf();
	}
}
