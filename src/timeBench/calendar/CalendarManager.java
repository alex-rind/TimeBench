package timeBench.calendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import timeBench.data.TemporalDataException;

/**
 * The interface used to access any CalendarManager that may exist.
 * 
 * <p>
 * Added:         2011-08-19 / TL<br>
 * Modifications: 2012-04-12 / TL / inf, sup absolute, identifier in context
 * </p>
 * 
 * @author Tim Lammarsch
 *
 */
public interface CalendarManager {
		
	/**
	 * Provides access to a singleton instance of the default calendar of the calendar manager.
	 * It does only create one instance and provides that one with every call.
	 * @return The calendar.
	 */
	public Calendar getDefaultCalendar();
		
	/**
	 * Provides access to a singleton instance of a calendar.
	 * It does only create one instance and provides that one with every call.
	 * @param localIdentifier the localIdentifier of the calendar
	 * @return The calendar.
	 */
	public Calendar getCalendar(int localIdentifier);
	
	/**
	 * Returns an array of global granularity identifiers that are provided by the calendar.
	 * @return array of global granularity identifiers
	 */
	public int[] getGlobalGranularityIdentifiers();
	
	/**
	 * Constructs a {@link Granule} from a given {@link Date}. Consider using the adequate constructor of
	 * {@link Granule} instead.
	 * @param input the {@link Date} used to generate the granule
	 * @param granularity granularity the {@link Granularity} to which the granule belongs
	 * @return the constructed {@link Granule}
	 * @throws TemporalDataException TemporalDataException thrown when granularities are not fully implemented
	 */ 
	public Granule createGranule(Date input, Granularity granularity) throws TemporalDataException;
	
	/**
	 * Constructs a {@link Granule} from inf to sup using a given {@linkplain Granule#MODE_INF_GRANULE mode} and
	 * for a given {@link Granularity}.
	 * Consider using the adequate constructor of {@link Granule} instead.
	 * @param inf the chronon that determines the start of the granule constructed
	 * @param sup the chronon that determines the end of the granule constructed
	 * @param mode the {@linkplain Granule#MODE_INF_GRANULE mode} used to construct the granule
	 * @param granularity the {@link Granularity} to use
	 * @return the constructed {@link Granule}
	 * @throws TemporalDataException TemporalDataException thrown when granularities are not fully implemented
	 */
    public Granule createGranule(long inf, long sup, int mode,
			Granularity granularity) throws TemporalDataException;
    
	/**
	 * Constructs several {@link Granule} objects from inf to sup that are at least partly in the given interval with
	 * a coverage of a least a given fraction and
	 * for a given {@link Granularity}. Consider using the adequate factory of {@link Granularity} instead.
	 * @param inf the chronon that determines the start of the {@link Granule} range constructed
	 * @param sup the chronon that determines the end of the {@link Granule} range constructed
	 * @param cover the coverage fraction of a granule needed to be included in the result
	 * @param granularity the {@link Granularity} to use
	 * @return the constructed array of {@link Granule}
	 * @throws TemporalDataException TemporalDataException thrown when granularities are not fully implemented
	 */
	public Granule[] createGranules(long inf, long sup, double cover,
			Granularity granularity) throws TemporalDataException;
	
	/**
	 * Constructs several {@link Granule} objects from other {@link Granule} objects for a given {@link Granularity}
	 * that can (and most likely
	 * will) be in a different {@link Granularity}. All {@link Granule} with
	 * a coverage of a least a given fraction are returned.
	 * Consider using the adequate factory of {@link Granularity} instead.
	 * @param granules the array of {@link Granule} used as source
	 * @param cover the coverage fraction of a granule needed to be included in the result
	 * @param granularity the {@link Granularity} to use
	 * @return the constructed array of {@link Granule}
	 * @throws TemporalDataException TemporalDataException thrown when granularities are not fully implemented
	 */
	public Granule[] createGranules(Granule[] granules, double cover,
			Granularity granularity) throws TemporalDataException;
	
	/**
	 * Calculate and return the identifier of a {@link Granule}. An identifier is a numeric label given in the context
	 * of the {@link Granularity}. Consider using the adequate method of
	 * {@link Granule} instead.
	 * @return the identifier
	 * @throws TemporalDataException thrown when granularities are not fully implemented
	 */
	public long createGranuleIdentifier(Granule granule) throws TemporalDataException;
	
	/**
	 * Calculate and return the human readable label of a {@link Granule}.
	 * Consider using the adequate method of
	 * {@link Granule} instead.
	 * @return the label
	 * @throws TemporalDataException thrown when granularities are not fully implemented
	 */
	public String createGranuleLabel(Granule granule) throws TemporalDataException;
	
	/**
	 * Calculate and return the inf of a {@link Granule}.
	 * @return the inf
	 * @throws TemporalDataException thrown when granularities are not fully implemented
	 */
	public long createInf(Granule granule) throws TemporalDataException;
	
	/**
	 * Calculate and return the sup of a {@link Granule}.
	 * @return the sup
	 * @throws TemporalDataException thrown when granularities are not fully implemented
	 */
	public long createSup(Granule granule) throws TemporalDataException;

	/**
	 * Provide the minimum identifier value that granules of a granularity can assume.
	 * @param granularity the granularity
	 * @return the minimum granule identifier value
	 * @throws TemporalDataException  thrown when granularity has illegal identifiers
	 */
	public long getMinGranuleIdentifier(Granularity granularity) throws TemporalDataException;

	/**
	 * Provide the maximum identifier value that granules of a granularity can assume.
	 * @param granularity the granularity
	 * @return the maximum granule identifier value
	 * @throws TemporalDataException thrown when granularity has illegal identifiers
	 */
	public long getMaxGranuleIdentifier(Granularity granularity) throws TemporalDataException;

	public long getMaxLengthInIdentifiers(Granularity granularity) throws TemporalDataException;
	
	public boolean contains(Granule granule, long chronon) throws TemporalDataException;

	public long getStartOfTime();
	
	public long getEndOfTime();

	public Granularity getBottomGranularity(Calendar calendar);

	public Granularity getTopGranularity(Calendar calendar);

	public int getGlobalCalendarManagerVersionIdentifier();

	public int getLocalCalendarManagerIdentifier();

	public int getLocalCalendarManagerVersionIdentifier();

	/**
	 * @param calendar
	 * @param granularityName
	 * @param contextGranularityName
	 * @return
	 */
	public Granularity getGranularity(Calendar calendar, String granularityName,
			String contextGranularityName);

	public void registerCalendar(int localIdentifier, Calendar calendar) throws TemporalDataException;

}