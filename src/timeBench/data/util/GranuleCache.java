package timeBench.data.util;

import prefuse.data.Table;
import prefuse.data.column.ObjectColumn;
import prefuse.data.event.EventConstants;
import prefuse.data.event.TableListener;
import timeBench.calendar.Calendar;
import timeBench.calendar.CalendarManagerFactory;
import timeBench.calendar.CalendarManagers;
import timeBench.calendar.Granularity;
import timeBench.calendar.Granule;
import timeBench.data.GenericTemporalElement;
import timeBench.data.TemporalDataException;
import timeBench.data.TemporalDataset;

/**
 * Cache for first granules of temporal elements. The cached granules are
 * automatically removed on changes of the backing temporal element table.
 * 
 * @author Rind
 * @see timeBench.data.TemporalElement#getGranule()
 */
public class GranuleCache {

    private ObjectColumn granules = new ObjectColumn(Granule[].class);

    private TemporalDataset tmpds;

    // TODO make configurable; move to TemporalDataset?
    private Calendar calendar = CalendarManagerFactory.getSingleton(
            CalendarManagers.JavaDate).getDefaultCalendar();

    public GranuleCache(TemporalDataset tmpds) {
        this.tmpds = tmpds;
        tmpds.getTemporalElements().getNodeTable()
                .addTableListener(new GranuleListener());
    }

    /**
     * <p>
     * Warning: reduced validity checks (to improve performance)
     * 
     * @param row
     * @return
     * @throws TemporalDataException
     */
    public Granule[] getGranules(int row) throws TemporalDataException {
        ensureRow(row);
        if (granules.get(row) == null) {
            GenericTemporalElement elem = tmpds.getTemporalElementByRow(row);
            if (elem.isAnchored()) {
                // TODO reuse granularity objects --> calendar responsible?
                Granularity g = new Granularity(calendar,
                        elem.getGranularityId(), elem.getGranularityContextId());
                granules.set(g.createGranules(elem.getInf(), elem.getSup()), row);
            } else {
                // TODO distinguish unknown from unanchored
                granules.set(null, row);
            }
        }
        return (Granule[]) granules.get(row);
    }

    public Granule getGranule(long id) throws TemporalDataException {
        GenericTemporalElement elem = tmpds.getTemporalElement(id);
        return (elem != null) ? this.getGranule(elem.getRow()) : null;
    }

    public void addGranule(int row, Granule granule) {
        ensureRow(row);
//        System.out.println("row: " + row + " size: " + granules.getRowCount());
        granules.set(new Granule[] {granule}, row);
    }
    
    private void ensureRow(int row) {
        // TODO check if +1 is necessary
        if (row >= granules.getRowCount()) {
            granules.setMaximumRow(row + 1);
        }
    }

    /**
     * Removes granules from the cache if the backing temporal element is
     * changed or deleted.
     * 
     * @author Rind
     */
    private class GranuleListener implements TableListener {

        @Override
        public void tableChanged(Table t, int start, int end, int col, int type) {
            // switch on the event type
            switch (type) {
            case EventConstants.UPDATE: {
                // do nothing if update on all columns, as this is only
                // used to indicate a non-measurable update.
                if (col == EventConstants.ALL_COLUMNS) {
                    break;
                } else {
                    for (int r = start; r <= end; ++r) {
                        if (r < GranuleCache.this.granules.getRowCount()) {
                            GranuleCache.this.granules.set(null, r);
                        }
                    }
                }
                break;
            }
            case EventConstants.DELETE:
                if (col == EventConstants.ALL_COLUMNS) {
                    // entire rows deleted
                    for (int r = start; r <= end; ++r) {
                        if (r < GranuleCache.this.granules.getRowCount()) {
                            GranuleCache.this.granules.set(null, r);
                        }
                    }
                }
                break;
            case EventConstants.INSERT:
                // nothing to do here
            } // switch
        }
    }
}
