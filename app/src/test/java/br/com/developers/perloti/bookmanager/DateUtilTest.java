package br.com.developers.perloti.bookmanager;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import br.com.developers.perloti.bookmanager.util.DateUtil;

/**
 * Created by perloti on 18/06/18.
 */

public class DateUtilTest {

    @Test(expected = RuntimeException.class)
    public void parseStringDateToDateRunTime() {
        Date date = DateUtil.stringToDate("2016-07-02");
        Assert.assertNull(date);
    }

    @Test()
    public void parseStringDateToDate() {
        String dateString = DateUtil.dateToString(new Date());
        Date date = DateUtil.stringToDate(dateString);
        Assert.assertNotNull(date);
    }


}
