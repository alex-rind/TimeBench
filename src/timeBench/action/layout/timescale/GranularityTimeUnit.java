package timeBench.action.layout.timescale;

import java.text.DateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import timeBench.calendar.Granularity;
import timeBench.calendar.Granule;
import timeBench.data.TemporalDataException;

public class GranularityTimeUnit extends TimeUnit {

    private static Logger logger = Logger.getLogger(GranularityTimeUnit.class);

    private long maxLengthInMillis;

    private Granularity granularity;
    private int factor;

    public GranularityTimeUnit(String name, Granularity granularity,
            DateFormat shortFormat, DateFormat longFormat, DateFormat fullFormat) {
        this(name, granularity, 1, shortFormat, longFormat, fullFormat);
    }

    public GranularityTimeUnit(String name, Granularity granularity,
            int factor, DateFormat shortFormat, DateFormat longFormat,
            DateFormat fullFormat) {
        super(name, shortFormat, longFormat, fullFormat);
        this.granularity = granularity;
        this.factor = factor;

        maxLengthInMillis = calculateLengthInMillis(granularity, factor);
    }

    private static long calculateLengthInMillis(Granularity granularity,
            int factor) {

        Granularity msWithContext = new Granularity(granularity.getCalendar(),
                granularity.getCalendar().getBottomGranularity()
                        .getIdentifier(), granularity.getIdentifier());
        try {
            Granule inf = new Granule(msWithContext.getMinGranuleIdentifier(),
                    msWithContext);
            Granule sup = new Granule(msWithContext.getMaxGranuleIdentifier(),
                    msWithContext);
            long length = sup.getSup() - inf.getInf() + 1;
            if (logger.isDebugEnabled())
                logger.debug("length: " + length + " granularity: "
                        + granularity);

            return length * factor;
        } catch (TemporalDataException e) {
            Logger.getLogger(GranularityTimeUnit.class).error(
                    "calculation of time unit length failed", e);
            throw new RuntimeException();
        }
    }

    @Override
    public long getMaxLengthInMillis() {
        return maxLengthInMillis;
    }
    
    private long getGranuleId(long date) throws TemporalDataException {
        Date oDate = new Date(date);
        Granule g = new Granule(oDate, granularity);
        long id = g.getIdentifier();
        if (logger.isDebugEnabled())
            logger.debug("in date: " + oDate + " id: " + id);
        return id;
    }

    @Override
    public long previous(long date) {
        try {
            long id = getGranuleId(date);

            long infId = (id / factor) * factor;
            Granule g = new Granule(infId, granularity);
            long prev = g.getInf();
            if (logger.isDebugEnabled())
                logger.debug("nf date: " + new Date(prev) + " Id: " + infId + " f: " + factor + " g: " + granularity);

            return prev;
        } catch (TemporalDataException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public long next(long date) {
        try {
            long id = getGranuleId(date);

            long supId = ((id / factor) + 1) * factor;
            Granule g = new Granule(supId, granularity);
            long next = g.getInf();
            if (logger.isDebugEnabled())
                logger.debug("su date: " + new Date(next) + " Id: " + supId + " f: " + factor + " g: " + granularity);

            return next;
        } catch (TemporalDataException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
