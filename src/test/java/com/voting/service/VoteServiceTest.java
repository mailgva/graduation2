package com.voting.service;

import com.voting.model.Resto;
import com.voting.model.User;
import com.voting.model.Vote;
import com.voting.util.exception.TooLateEcxeption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles({"datajpa","postgres"})
public class VoteServiceTest {

    @Autowired
    private VoteService service;

    @Autowired
    private UserService userService;

    @Autowired
    private RestoService restoService;


    @Test
    public void create() throws ParseException {
        User user = userService.get(100001);
        Resto resto = restoService.get(100003);

        Calendar calendar = new GregorianCalendar(2018,Calendar.NOVEMBER,03);
        Date date = calendar.getTime();

        Vote vote = new Vote(null, user, resto, date, LocalDateTime.now());
        service.create(vote, 100001);
    }

    @Test(expected = TooLateEcxeption.class)
    public void update() {
        User user = userService.get(100001);
        Resto resto = restoService.get(100003);

        Calendar calendar = new GregorianCalendar(2018,Calendar.NOVEMBER,07);
        Date date = calendar.getTime();

        Vote vote = new Vote(null, user, resto, date, LocalDateTime.now());
        service.create(vote, 100001);

        Vote newVote = service.get(100127, 100001);
        newVote.setResto(restoService.get(100004));
        calendar = new GregorianCalendar(2018,Calendar.NOVEMBER,01);
        date = calendar.getTime();
        newVote.setDate(date);
        service.update(newVote, 100001);


    }


    @Test
    public void get() {
        System.out.println(service.get(100030,100001));

    }

    @Test
    public void delete() {
    }

    @Test
    public void getBetweenDates() {
    }

    @Test
    public void getBetweenDateTimes() {
    }

    @Test
    public void getAll() {
    }


    @Test
    public void getByDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2018-11-11");
        Vote vote = service.getByDate(date, 100000);
        //System.out.println(vote);
    }



}