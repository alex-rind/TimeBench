package timeBench.data.oo;

import timeBench.calendar.Granularity;


/**
 * The class for an indeterminate interval. This is a stub, users have to work
 * manually at the moment
 * 
 * <p>
 * Added:         2011-07-19 / TL<br>
 * Modifications: 
 * </p>
 * 
 * @author Tim Lammarsch
 *
 */
public class IndeterminateInterval extends AnchoredTemporalElement {
	protected IndeterminateInterval(Interval start, Interval end, Span min, Span max, Granularity granularity) {
		
		// TODO Validity check
		
		super(start.getInf().getChronon(),end.getSup().getChronon(),granularity);
		parts.add(start);
		parts.add(end);
		parts.add(min);
		parts.add(max);
	}
}
